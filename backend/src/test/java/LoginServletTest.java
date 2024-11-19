
import org.junit.Before;
import org.junit.Test;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LoginServletTest {
    private LoginServlet loginServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @Before
    public void setUp() throws Exception {
        loginServlet = new LoginServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
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
        assertEquals("Login successful!", stringWriter.toString().trim());
    }

    @Test
    public void testInvalidUsername() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");
        loginServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("Login Failed", stringWriter.toString().trim());
    }

    @Test
    public void testInvalidPassword() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        loginServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("Login Failed", stringWriter.toString().trim());
    }

    @Test
    public void testContentTypeValidation() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        loginServlet.doPost(request, response);
        verify(response).setContentType("text/html");
    }

    @Test
    public void testMultipleFailedAttempts() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        for (int i = 0; i < 5; i++) {
            loginServlet.doPost(request, response);
        }
        verify(response, times(5)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("Login Failed", stringWriter.toString().trim());
    }
}
