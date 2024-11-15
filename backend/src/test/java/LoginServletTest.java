import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.servlet.http.HttpServletResponse;

public class LoginServletTest {
    private LoginServlet loginServlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        loginServlet = new LoginServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testValidLogin() {
        request.setParameter("username", "admin");
        request.setParameter("password", "password123");
        loginServlet.doPost(request, response);
        
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertEquals("Login Successful!", response.getContentAsString().trim());
    }

    @Test
    public void testInvalidUsername() {
        request.setParameter("username", "wronguser");
        request.setParameter("password", "password123");
        loginServlet.doPost(request, response);
        
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Login Failed. Invalid username or password.", response.getContentAsString().trim());
    }

    @Test
    public void testInvalidPassword() {
        request.setParameter("username", "admin");
        request.setParameter("password", "wrongpassword");
        loginServlet.doPost(request, response);
        
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Login Failed. Invalid username or password.", response.getContentAsString().trim());
    }

    @Test
    public void testContentType() {
        request.setParameter("username", "admin");
        request.setParameter("password", "password123");
        loginServlet.doPost(request, response);
        
        assertEquals("text/html", response.getContentType());
    }

    @Test
    public void testLoginAttemptsLimit() {
        for (int i = 0; i < 6; i++) {
            request = new MockHttpServletRequest();
            response = new MockHttpServletResponse();
            request.setParameter("username", "admin");
            request.setParameter("password", "wrongpassword");
            loginServlet.doPost(request, response);
        }
        
        assertEquals(HttpServletResponse.SC_TOO_MANY_REQUESTS, response.getStatus());
        assertEquals("Account temporarily locked due to multiple failed login attempts. Please try again later.", 
                    response.getContentAsString().trim());
    }
}