
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginServletTest {

    private LoginServlet loginServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @Before
    public void setUp() throws Exception {
        loginServlet = new LoginServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testSuccessfulLogin() {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).println("Login Successful!");
    }

    @Test
    public void testInvalidUsername() {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).println("Login Failed. Invalid username or password.");
    }

    @Test
    public void testInvalidPassword() {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).println("Login Failed. Invalid username or password.");
    }

    @Test
    public void testContentTypeValidation() {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        verify(response).setContentType("text/html");
    }

    @Test
    public void testTooManyFailedAttempts() {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        
        for (int i = 0; i < 5; i++) {
            loginServlet.doPost(request, response);
        }
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(429);
        verify(writer).println("Account temporarily locked due to multiple failed login attempts. Please try again later.");
    }
}
