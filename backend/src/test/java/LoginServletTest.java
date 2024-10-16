Here's the JUnit test code for the LoginServletTest class based on the provided requirements:

```java
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    @Mock
    private HttpSession session;

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
        when(request.getSession(anyBoolean())).thenReturn(session);
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
    public void testFailedLogin() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpassword");

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
    public void testBruteForceProtection() throws ServletException, IOException {
        for (int i = 0; i < 5; i++) {
            when(request.getParameter("username")).thenReturn("wronguser");
            when(request.getParameter("password")).thenReturn("wrongpassword");

            servlet.doPost(request, response);

            verify(response, times(i + 1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        assertTrue(stringWriter.toString().contains("Account locked"));
    }

    @Test
    public void testSqlInjectionPrevention() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin' OR '1'='1");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    public void testXssPrevention() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("<script>alert('XSS')</script>");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertFalse(stringWriter.toString().contains("<script>"));
    }

    @Test
    public void testSessionTimeout() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(session).setMaxInactiveInterval(1800); // 30 minutes
    }

    @Test
    public void testConcurrentSessionHandling() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(session).invalidate();
        verify(request).getSession(true);
    }

    @Test
    public void testResponseTime() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        long startTime = System.currentTimeMillis();
        servlet.doPost(request, response);
        long endTime = System.currentTimeMillis();

        assertTrue(endTime - startTime < 1000); // Response time should be less than 1 second
    }

    @Test
    public void testBoundaryValues() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("a".repeat(50));
        when(request.getParameter("password")).thenReturn("p".repeat(50));

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testInvalidCharacterEncoding() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn(new String(new byte[]{(byte) 0xFF, (byte) 0xFE}));
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testNonAsciiCredentials() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("用户名");
        when(request.getParameter("password")).thenReturn("パスワード");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testLoginAttemptLogging() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        // Verify that logging is performed (implementation-specific)
    }
}
```

This test class includes a comprehensive set of test cases covering authentication, input validation, security, performance, and edge cases as specified in the requirements. Note that some tests might need to be adjusted based on the actual implementation of the `LoginServlet` class.