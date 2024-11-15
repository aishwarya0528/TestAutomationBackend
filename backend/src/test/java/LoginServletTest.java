import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.mockito.Mockito.*;

public class LoginServletTest {
    
    private LoginServlet loginServlet;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        loginServlet = new LoginServlet();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(200);
        verify(response).setContentType("text/html");
        assert(stringWriter.toString().contains("Login Successful!"));
    }

    @Test
    void testInvalidUsername() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(401);
        verify(response).setContentType("text/html");
        assert(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    void testInvalidPassword() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(401);
        verify(response).setContentType("text/html");
        assert(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    void testContentTypeValidation() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        verify(response).setContentType("text/html");
    }

    @Test
    void testMultipleFailedAttempts() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        
        for(int i = 0; i < 5; i++) {
            loginServlet.doPost(request, response);
            verify(response, times(i + 1)).setStatus(401);
            assert(stringWriter.toString().contains("Login Failed"));
        }
        
        when(request.getParameter("password")).thenReturn("password123");
        loginServlet.doPost(request, response);
        verify(response).setStatus(200);
        assert(stringWriter.toString().contains("Login Successful!"));
    }
}