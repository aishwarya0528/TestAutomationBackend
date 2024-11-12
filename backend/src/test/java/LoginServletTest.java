
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import javax.servlet.http.*;
import org.junit.*;
import org.mockito.*;

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
    public void testValidLogin() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login Successful!"));
        assertTrue(stringWriter.toString().contains("Welcome, admin"));
    }

    @Test
    public void testInvalidUsername() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    public void testInvalidPassword() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    public void testContentTypeValidation() throws Exception {
        loginServlet.doPost(request, response);

        verify(response).setContentType("text/html");
    }

    @Test
    public void testLoginAttemptLimit() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        for (int i = 0; i < 5; i++) {
            loginServlet.doPost(request, response);
            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
            stringWriter.getBuffer().setLength(0);
        }

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
        assertTrue(stringWriter.toString().contains("Account temporarily locked due to multiple failed login attempts. Please try again later."));
    }
}
