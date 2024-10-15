Here are the JUnit test cases based on the provided LoginServlet.java file:

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

    private LoginServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        servlet = new LoginServlet();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testLOGIN_001() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login Successful!"));
        assertTrue(stringWriter.toString().contains("Welcome, admin!"));
    }

    @Test
    public void testLOGIN_002() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password"));
    }

    @Test
    public void testLOGIN_003() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password"));
    }

    @Test
    public void testLOGIN_004() throws Exception {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password"));
    }

    @Test
    public void testLOGIN_005() throws Exception {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password"));
    }

    @Test
    public void testLOGIN_006() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("");
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password"));
    }

    @Test
    public void testLOGIN_007() throws Exception {
        when(request.getParameter("username")).thenReturn("admin' --");
        when(request.getParameter("password")).thenReturn("anypassword");
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
        assertTrue(stringWriter.toString().contains("Invalid username or password"));
    }

    @Test
    public void testLOGIN_008() throws Exception {
        when(request.getParameter("username")).thenReturn("<script>alert('XSS')</script>");
        when(request.getParameter("password")).thenReturn("password123");
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertFalse(stringWriter.toString().contains("<script>"));
    }

    @Test
    public void testLOGIN_011() throws Exception {
        servlet.doPost(request, response);
        verify(response).setContentType("text/html");
    }

    @Test
    public void testLOGIN_013() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Invalid username or password"));
    }
}
```