
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServletTest {
    private LoginServlet loginServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        loginServlet = new LoginServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testSuccessfulLogin() throws IOException, ServletException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        loginServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login successful!"));
    }

    @Test
    void testInvalidUsername() throws IOException, ServletException {
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("password123");
        loginServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    void testInvalidPassword() throws IOException, ServletException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        loginServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }

    @Test
    void testContentTypeValidation() throws IOException, ServletException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password123");
        loginServlet.doPost(request, response);
        verify(response).setContentType("text/html");
    }

    @Test
    void testMultipleFailedAttempts() throws IOException, ServletException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        for (int i = 0; i < 5; i++) {
            loginServlet.doPost(request, response);
        }
        verify(response, times(5)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Login Failed"));
    }
}
