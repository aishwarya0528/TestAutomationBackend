Here's the JUnit test code for the LoginServlet class:

```java
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

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login Successful"));
    }

    @Test
    public void testFailedLoginIncorrectPassword() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testFailedLoginIncorrectUsername() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testFailedLoginBothIncorrect() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testEmptyCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testNullCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testSqlInjectionAttempt() throws Exception {
        when(request.getParameter("username")).thenReturn("admin' --");
        when(request.getParameter("password")).thenReturn("password123");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testXssAttempt() throws Exception {
        when(request.getParameter("username")).thenReturn("<script>alert('XSS')</script>");
        when(request.getParameter("password")).thenReturn("password123");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testLongInputValues() throws Exception {
        when(request.getParameter("username")).thenReturn("a".repeat(1000));
        when(request.getParameter("password")).thenReturn("b".repeat(1000));

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testSpecialCharactersInInput() throws Exception {
        when(request.getParameter("username")).thenReturn("admin!@#$%^&*()");
        when(request.getParameter("password")).thenReturn("pass!@#$%^&*()");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testCaseSensitivity() throws Exception {
        when(request.getParameter("username")).thenReturn("Admin");
        when(request.getParameter("password")).thenReturn("Password123");

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }
}
```