
import static org.junit.Assert.*;
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
    public void testValidLogin() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        loginServlet.doPost(request, response);

        verify(response).setStatus(200);
        assertTrue(stringWriter.toString().contains("Login Successful!"));
    }

    @Test
    public void testInvalidUsername() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");

        loginServlet.doPost(request, response);

        verify(response).setStatus(401);
        assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    public void testInvalidPassword() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        loginServlet.doPost(request, response);

        verify(response).setStatus(401);
        assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    public void testContentTypeValidation() throws Exception {
        loginServlet.doGet(request, response);

        verify(response).setContentType("text/html");
    }

    @Test
    public void testLoginAttemptLimit() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        for (int i = 0; i < 6; i++) {
            loginServlet.doPost(request, response);
        }

        verify(response).setStatus(429);
        assertTrue(stringWriter.toString().contains("Account temporarily locked due to multiple failed login attempts. Please try again later."));
    }
}
