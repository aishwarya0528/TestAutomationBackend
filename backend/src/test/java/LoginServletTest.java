
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

public class LoginServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    private LoginServlet loginServlet;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        loginServlet = new LoginServlet();
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        String username = "admin";
        String password = "password123";
        
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).println("Login successful!");
    }

    @Test
    public void testInvalidUsername() throws Exception {
        String username = "wronguser";
        String password = "password123";
        
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).println("Login Failed");
    }

    @Test
    public void testInvalidPassword() throws Exception {
        String username = "admin";
        String password = "wrongpassword";
        
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).println("Login Failed");
    }

    @Test
    public void testMultipleFailedAttempts() throws Exception {
        String username = "admin";
        String password = "wrongpassword";
        
        for (int i = 0; i < 5; i++) {
            when(request.getParameter("username")).thenReturn(username);
            when(request.getParameter("password")).thenReturn(password);
            loginServlet.doPost(request, response);
            
            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(writer).println("Login Failed");
        }
    }
}
