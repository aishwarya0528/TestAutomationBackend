
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import javax.servlet.http.*;
import org.junit.*;
import org.mockito.*;

public class LoginServletTest {

    private LoginServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @Before
    public void setUp() throws Exception {
        servlet = new LoginServlet();
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
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Login Successful"));
    }

    @Test
    public void testFailedLogin() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpass");
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testEmptyCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testNullCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testSuccessfulLoginStatus() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testFailedLoginStatus() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpass");
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testSuccessfulLoginContent() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("<h1>Login Successful!</h1>"));
    }

    @Test
    public void testFailedLoginContent() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpass");
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("<h1>Login Failed</h1>"));
    }

    @Test
    public void testSuccessfulLoginWelcomeMessage() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Welcome, admin!"));
    }

    @Test
    public void testLongUsernamePassword() throws Exception {
        String longString = new String(new char[1000]).replace("\0", "a");
        when(request.getParameter("username")).thenReturn(longString);
        when(request.getParameter("password")).thenReturn(longString);
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testSpecialCharacters() throws Exception {
        when(request.getParameter("username")).thenReturn("user@!#$%^&*()");
        when(request.getParameter("password")).thenReturn("pass@!#$%^&*()");
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testSQLInjectionAttempt() throws Exception {
        when(request.getParameter("username")).thenReturn("admin' --");
        when(request.getParameter("password")).thenReturn("anything");
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test(timeout = 1000)
    public void testLoginResponseTime() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
    }

    @Test
    public void testConcurrentLogins() throws Exception {
        // This test would require a more complex setup to simulate concurrent requests
    }

    @Test
    public void testMalformedRequest() throws Exception {
        // This test would require simulating a malformed request, which is not easily done with mocks
    }

    @Test
    public void testMissingParameters() throws Exception {
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testPasswordExposure() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
        assertFalse(stringWriter.toString().contains("password123"));
    }

    @Test
    public void testSessionManagement() throws Exception {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
        verify(request).getSession();
    }
}
