import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.mockito.Mockito.*;

public class LoginServletTest {
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    private LoginServlet loginServlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        loginServlet = new LoginServlet();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        loginServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("text/html");
        assert(stringWriter.toString().contains("Login successful"));
    }

    @Test
    public void testInvalidUsername() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");
        loginServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("text/html");
        assert(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testInvalidPassword() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        loginServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("text/html");
        assert(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testMultipleFailedAttempts() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        for (int i = 0; i < 5; i++) {
            loginServlet.doPost(request, response);
        }
        loginServlet.doPost(request, response);
        verify(response, times(5)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response, times(1)).setStatus(401);
        verify(response, atLeastOnce()).setContentType("text/html");
    }
}