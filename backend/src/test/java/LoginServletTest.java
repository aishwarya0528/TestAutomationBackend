
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private LoginServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        servlet = new LoginServlet();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testSuccessfulLogin() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login Successful!"));
        assertTrue(stringWriter.toString().contains("Welcome, admin!"));
    }

    @Test
    public void testFailedLoginIncorrectPassword() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password"));
    }

    @Test
    public void testFailedLoginIncorrectUsername() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password"));
    }

    @Test
    public void testEmptyCredentials() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testSqlInjectionAttempt() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin' --");
        when(request.getParameter("password")).thenReturn("anypassword");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testXssAttempt() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("<script>alert('XSS')</script>");
        when(request.getParameter("password")).thenReturn("anypassword");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertFalse(stringWriter.toString().contains("<script>"));
    }

    @Test
    public void testLongInputStrings() throws ServletException, IOException {
        String longString = new String(new char[1000]).replace("\0", "a");
        when(request.getParameter("username")).thenReturn(longString);
        when(request.getParameter("password")).thenReturn(longString);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testSpecialCharactersInInput() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin!@#$%^&*()");
        when(request.getParameter("password")).thenReturn("password123!@#$%^&*()");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testCaseSensitivity() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("Admin");
        when(request.getParameter("password")).thenReturn("Password123");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }
}
