
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import javax.servlet.http.*;
import java.io.*;

public class LoginServletTest {

    @Test
    public void testSuccessfulLogin() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        LoginServlet loginServlet = new LoginServlet();
        loginServlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login Successful!"));
        assertTrue(stringWriter.toString().contains("Welcome, admin!"));
    }

    @Test
    public void testFailedLogin() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("username")).thenReturn("invaliduser");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        LoginServlet loginServlet = new LoginServlet();
        loginServlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password. Try again."));
        assertTrue(stringWriter.toString().contains("Go Back to Login"));
    }

    @Test
    public void testMultipleFailedLoginAttempts() throws Exception {
        LoginServlet loginServlet = new LoginServlet();
        
        for (int i = 0; i < 5; i++) {
            simulateFailedLoginAttempt(loginServlet);
        }
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("username")).thenReturn("invaliduser");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        loginServlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
        assertTrue(stringWriter.toString().contains("Account temporarily locked due to multiple failed login attempts. Please try again later."));
    }

    private void simulateFailedLoginAttempt(LoginServlet loginServlet) throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("username")).thenReturn("invaliduser");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        loginServlet.doPost(request, response);
    }
}
