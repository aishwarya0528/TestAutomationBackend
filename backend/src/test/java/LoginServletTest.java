import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.*;
import javax.servlet.http.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoginServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    private LoginServlet servlet;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        servlet = new LoginServlet();
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).println(contains("<h1>Login Successful!</h1>"));
        verify(writer).println(contains("<p>Welcome, admin!</p>"));
    }

    @Test
    public void testFailedLogin() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpass");

        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).println(contains("<h1>Login Failed</h1>"));
        verify(writer).println(contains("<p>Invalid username or password. Try again.</p>"));
    }

    @Test
    public void testEmptyCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");

        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).println(contains("<h1>Login Failed</h1>"));
        verify(writer).println(contains("<p>Invalid username or password. Try again.</p>"));
    }

    @Test
    public void testNullCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).println(contains("<h1>Login Failed</h1>"));
        verify(writer).println(contains("<p>Invalid username or password. Try again.</p>"));
    }
}