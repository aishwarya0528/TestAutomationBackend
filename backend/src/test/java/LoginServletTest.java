import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class LoginServletTest {

    @Test
    public void testSuccessfulLogin() {
        LoginServlet loginServlet = new LoginServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        when(response.getWriter()).thenReturn(writer);
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login Successful!"));
    }

    @Test
    public void testInvalidUsername() {
        LoginServlet loginServlet = new LoginServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");
        when(response.getWriter()).thenReturn(writer);
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    public void testInvalidPassword() {
        LoginServlet loginServlet = new LoginServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        when(response.getWriter()).thenReturn(writer);
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    public void testContentTypeValidation() {
        LoginServlet loginServlet = new LoginServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        loginServlet.doPost(request, response);
        
        verify(response).setContentType("text/html");
    }

    @Test
    public void testLoginAttemptsLockout() {
        LoginServlet loginServlet = new LoginServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        when(response.getWriter()).thenReturn(writer);
        
        for (int i = 0; i < 6; i++) {
            loginServlet.doPost(request, response);
        }
        
        verify(response).setStatus(429);
        assertTrue(stringWriter.toString().contains("Account temporarily locked due to multiple failed login attempts. Please try again later."));
    }
}