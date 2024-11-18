import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.mockito.Mockito.*;

public class LoginServletTest {
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    private LoginServlet loginServlet;
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
        
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("Login Successful!", stringWriter.toString().trim());
    }

    @Test
    void testInvalidUsername() throws Exception {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("Login Failed", stringWriter.toString().trim());
    }

    @Test
    void testInvalidPassword() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        
        loginServlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("Login Failed", stringWriter.toString().trim());
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
        
        for (int i = 0; i < 5; i++) {
            loginServlet.doPost(request, response);
            verify(response, times(i + 1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            assertEquals("Login Failed", stringWriter.toString().trim());
            stringWriter.getBuffer().setLength(0);
        }
    }
}