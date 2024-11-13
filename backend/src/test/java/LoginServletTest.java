Here's the JUnit test code for the LoginServlet class:

```java
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import javax.servlet.http.HttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class LoginServletTest {
    private LoginServlet loginServlet;
    
    @Before
    public void setUp() {
        loginServlet = new LoginServlet();
    }

    @Test
    public void testSuccessfulLogin() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.setParameter("username", "admin");
        request.setParameter("password", "password123");
        
        loginServlet.doPost(request, response);
        
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertTrue(response.getContentAsString().contains("Login Successful!"));
        assertTrue(response.getContentAsString().contains("admin"));
    }

    @Test
    public void testInvalidUsername() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.setParameter("username", "wronguser");
        request.setParameter("password", "password123");
        
        loginServlet.doPost(request, response);
        
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertTrue(response.getContentAsString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    public void testInvalidPassword() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.setParameter("username", "admin");
        request.setParameter("password", "wrongpassword");
        
        loginServlet.doPost(request, response);
        
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertTrue(response.getContentAsString().contains("Login Failed. Invalid username or password."));
    }

    @Test
    public void testEmptyUsername() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.setParameter("username", "");
        request.setParameter("password", "password123");
        
        loginServlet.doPost(request, response);
        
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertTrue(response.getContentAsString().contains("Username and password are required"));
    }

    @Test
    public void testEmptyPassword() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.setParameter("username", "admin");
        request.setParameter("password", "");
        
        loginServlet.doPost(request, response);
        
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertTrue(response.getContentAsString().contains("Username and password are required"));
    }

    @Test
    public void testLoginAttemptLimit() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        for (int i = 0; i < 5; i++) {
            request.setParameter("username", "admin");
            request.setParameter("password", "wrongpassword");
            loginServlet.doPost(request, response);
        }
        
        assertEquals(HttpServletResponse.SC_TOO_MANY_REQUESTS, response.getStatus());
        assertTrue(response.getContentAsString().contains("Account temporarily locked due to multiple failed login attempts. Please try again later."));
        
        request.setParameter("username", "admin");
        request.setParameter("password", "password123");
        loginServlet.doPost(request, response);
        
        assertEquals(HttpServletResponse.SC_TOO_MANY_REQUESTS, response.getStatus());
    }

    @Test
    public void testContentTypeValidation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.setParameter("username", "admin");
        request.setParameter("password", "password123");
        
        loginServlet.doPost(request, response);
        
        assertEquals("text/html", response.getContentType());
    }
}
```