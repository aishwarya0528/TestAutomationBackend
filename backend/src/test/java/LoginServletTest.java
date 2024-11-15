
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void setUp() throws Exception {
        loginServlet = new LoginServlet();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testValidLogin() throws Exception {
        Mockito.when(request.getParameter("username")).thenReturn("admin");
        Mockito.when(request.getParameter("password")).thenReturn("password123");

        loginServlet.doPost(request, response);

        Mockito.verify(response).setStatus(200);
        assertTrue(stringWriter.toString().contains("Login Successful!"));
    }

    @Test
    public void testInvalidUsername() throws Exception {
        Mockito.when(request.getParameter("username")).thenReturn("wronguser");
        Mockito.when(request.getParameter("password")).thenReturn("password123");

        loginServlet.doPost(request, response);

        Mockito.verify(response).setStatus(401);
        assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    public void testInvalidPassword() throws Exception {
        Mockito.when(request.getParameter("username")).thenReturn("admin");
        Mockito.when(request.getParameter("password")).thenReturn("wrongpassword");

        loginServlet.doPost(request, response);

        Mockito.verify(response).setStatus(401);
        assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    public void testContentType() throws Exception {
        loginServlet.doPost(request, response);

        Mockito.verify(response).setContentType("text/html");
    }

    @Test
    public void testLoginAttemptLimit() throws Exception {
        Mockito.when(request.getParameter("username")).thenReturn("admin");
        Mockito.when(request.getParameter("password")).thenReturn("wrongpassword");

        for (int i = 0; i < 5; i++) {
            loginServlet.doPost(request, response);
        }

        Mockito.verify(response).setStatus(429);
        assertTrue(stringWriter.toString().contains("Account temporarily locked due to multiple failed login attempts. Please try again later."));
    }
}
