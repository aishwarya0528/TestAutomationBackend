
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
    }

    @Test
    public void testFailedLoginIncorrectPassword() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testFailedLoginIncorrectUsername() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
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
    public void testNullCredentials() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testCaseSensitivity() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("ADMIN");
        when(request.getParameter("password")).thenReturn("PASSWORD123");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testWhitespaceHandling() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn(" admin ");
        when(request.getParameter("password")).thenReturn(" password123 ");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testSpecialCharactersInInput() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin<script>");
        when(request.getParameter("password")).thenReturn("password123'");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testLongInputStrings() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("a".repeat(1000));
        when(request.getParameter("password")).thenReturn("p".repeat(1000));

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testContentTypeVerification() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
    }

    @Test
    public void testResponseContentVerification() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Welcome, admin!"));

        reset(request, response);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Invalid username or password"));
    }

    @Test
    public void testMultipleConsecutiveFailedAttempts() throws ServletException, IOException {
        String[][] credentials = {
                {"wronguser1", "wrongpass1"},
                {"wronguser2", "wrongpass2"},
                {"wronguser3", "wrongpass3"}
        };

        for (String[] cred : credentials) {
            reset(request, response);
            stringWriter = new StringWriter();
            writer = new PrintWriter(stringWriter);
            when(response.getWriter()).thenReturn(writer);

            when(request.getParameter("username")).thenReturn(cred[0]);
            when(request.getParameter("password")).thenReturn(cred[1]);

            servlet.doPost(request, response);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            assertTrue(stringWriter.toString().contains("Login Failed"));
        }
    }

    @Test
    public void testHttpMethodNotAllowed() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void testSessionManagement() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(request).getSession(true);
    }
}
