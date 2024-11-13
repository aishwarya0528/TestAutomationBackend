import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginServletTest {
    private LoginServlet loginServlet;
    
    @Before
    public void setUp() {
        loginServlet = new LoginServlet();
    }

    @Test
    public void testSuccessfulLogin() {
        Response response = loginServlet.doPost("admin", "password123");
        assertEquals(200, response.getStatus());
        assertTrue(response.getContent().contains("Login Successful!"));
        assertTrue(response.getContent().contains("admin"));
    }

    @Test
    public void testInvalidUsername() {
        Response response = loginServlet.doPost("wronguser", "password123");
        assertEquals(401, response.getStatus());
        assertEquals("Login Failed. Invalid username or password.", response.getContent());
    }

    @Test
    public void testInvalidPassword() {
        Response response = loginServlet.doPost("admin", "wrongpassword");
        assertEquals(401, response.getStatus());
        assertEquals("Login Failed. Invalid username or password.", response.getContent());
    }

    @Test
    public void testContentType() {
        Response response = loginServlet.doPost("admin", "password123");
        assertEquals("text/html", response.getContentType());
    }

    @Test
    public void testLoginAttemptLimit() {
        for (int i = 0; i < 5; i++) {
            Response response = loginServlet.doPost("admin", "wrongpass");
            assertEquals(401, response.getStatus());
        }
        Response response = loginServlet.doPost("admin", "wrongpass");
        assertEquals(429, response.getStatus());
        assertEquals("Account temporarily locked due to multiple failed login attempts. Please try again later.", 
                    response.getContent());
    }

    @Test
    public void testAccountLockoutDuration() {
        for (int i = 0; i < 6; i++) {
            loginServlet.doPost("admin", "wrongpass");
        }
        
        Response response = loginServlet.doPost("admin", "password123");
        assertEquals(429, response.getStatus());
        
        simulateTimePass(15);
        
        response = loginServlet.doPost("admin", "password123");
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testPasswordSecurity() {
        Response response = loginServlet.doPost("admin", "wrongpassword");
        assertFalse(response.getContent().contains("password123"));
    }
}