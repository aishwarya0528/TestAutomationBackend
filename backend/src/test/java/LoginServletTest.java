
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.*;
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

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login Successful!"));
        assertTrue(stringWriter.toString().contains("Welcome, admin!"));
    }

    @Test
    public void testFailedLogin() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        loginServlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password. Try again."));
    }

    @Test
    public void testEmptyCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");

        loginServlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password. Try again."));
    }
}
