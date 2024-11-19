Here are the JUnit test cases for the LoginServlet class:

```java
import static org.mockito.Mockito.*;
import javax.servlet.http.*;
import org.junit.Before;
import org.junit.Test;
import java.io.PrintWriter;

public class LoginServletTest {

    private LoginServlet loginServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @Before
    public void setUp() throws Exception {
        loginServlet = new LoginServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testSuccessfulLogin() {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).println("Login Successful!");
    }

    @Test
    public void testInvalidUsername() {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(401);
        verify(writer).println("Login Failed");
    }

    @Test
    public void testInvalidPassword() {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(401);
        verify(writer).println("Login Failed");
    }

    @Test
    public void testContentTypeValidation() {
        loginServlet.doPost(request, response);
        
        verify(response).setContentType("text/html");
    }

    @Test
    public void testMultipleFailedAttempts() {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        
        for(int i = 0; i < 5; i++) {
            loginServlet.doPost(request, response);
        }
        
        loginServlet.doPost(request, response);
        verify(response).setStatus(401);
        verify(writer).println("Login Failed");
    }
}
```