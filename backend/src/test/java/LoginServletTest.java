Here are the JUnit test cases for the LoginServlet class:

```java
import org.junit.Test;
import org.mockito.Mockito;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class LoginServletTest {

    @Test
    public void testSuccessfulLogin() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        Mockito.when(request.getParameter("username")).thenReturn("admin");
        Mockito.when(request.getParameter("password")).thenReturn("password123");
        Mockito.when(response.getWriter()).thenReturn(writer);

        LoginServlet servlet = new LoginServlet();
        servlet.doPost(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login Successful!"));
        assertTrue(stringWriter.toString().contains("Welcome, admin!"));
    }

    @Test
    public void testFailedLoginWithInvalidCredentials() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        Mockito.when(request.getParameter("username")).thenReturn("wronguser");
        Mockito.when(request.getParameter("password")).thenReturn("wrongpass");
        Mockito.when(response.getWriter()).thenReturn(writer);

        LoginServlet servlet = new LoginServlet();
        servlet.doPost(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password. Try again."));
        assertTrue(stringWriter.toString().contains("Go Back to Login"));
    }

    @Test
    public void testLoginWithMissingUsername() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        Mockito.when(request.getParameter("username")).thenReturn(null);
        Mockito.when(request.getParameter("password")).thenReturn("password123");
        Mockito.when(response.getWriter()).thenReturn(writer);

        LoginServlet servlet = new LoginServlet();
        servlet.doPost(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password. Try again."));
    }

    @Test
    public void testLoginWithMissingPassword() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        Mockito.when(request.getParameter("username")).thenReturn("admin");
        Mockito.when(request.getParameter("password")).thenReturn(null);
        Mockito.when(response.getWriter()).thenReturn(writer);

        LoginServlet servlet = new LoginServlet();
        servlet.doPost(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password. Try again."));
    }
}
```