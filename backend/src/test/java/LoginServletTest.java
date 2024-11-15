import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockito.Mockito;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LoginServletTest {
    private LoginServlet loginServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        loginServlet = new LoginServlet();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        Mockito.when(request.getParameter("username")).thenReturn("admin");
        Mockito.when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login Successful!"));
        assertTrue(stringWriter.toString().contains("Welcome, admin"));
    }

    @Test
    void testInvalidUsername() throws Exception {
        Mockito.when(request.getParameter("username")).thenReturn("wronguser");
        Mockito.when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        Mockito.verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    void testInvalidPassword() throws Exception {
        Mockito.when(request.getParameter("username")).thenReturn("admin");
        Mockito.when(request.getParameter("password")).thenReturn("wrongpassword");
        
        loginServlet.doPost(request, response);
        
        Mockito.verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    void testContentType() throws Exception {
        Mockito.when(request.getParameter("username")).thenReturn("admin");
        Mockito.when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        Mockito.verify(response).setContentType("text/html");
    }

    @Test
    void testTooManyFailedAttempts() throws Exception {
        for (int i = 0; i < 5; i++) {
            Mockito.when(request.getParameter("username")).thenReturn("admin");
            Mockito.when(request.getParameter("password")).thenReturn("wrongpassword");
            loginServlet.doPost(request, response);
        }
        
        loginServlet.doPost(request, response);
        
        Mockito.verify(response, Mockito.atLeastOnce()).setStatus(429);
        assertTrue(stringWriter.toString().contains("Account temporarily locked due to multiple failed login attempts. Please try again later."));
    }
}