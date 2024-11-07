
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class LoginServletTest {

    @Test
    public void testSuccessfulLogin() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = new StringWriter();

        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        LoginServlet servlet = new LoginServlet();
        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(writer.toString().contains("Login Successful!"));
        assertTrue(writer.toString().contains("Welcome, admin!"));
    }

    @Test
    public void testFailedLogin() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = new StringWriter();

        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        LoginServlet servlet = new LoginServlet();
        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(writer.toString().contains("Login Failed"));
        assertTrue(writer.toString().contains("Invalid username or password. Try again."));
    }

    @Test
    public void testEmptyCredentials() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = new StringWriter();

        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        LoginServlet servlet = new LoginServlet();
        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(writer.toString().contains("Login Failed"));
        assertTrue(writer.toString().contains("Invalid username or password. Try again."));
    }
}
