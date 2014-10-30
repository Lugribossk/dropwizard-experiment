package bo.gotthardt.jersey.filter;

import com.google.common.io.BaseEncoding;
import com.google.common.net.HttpHeaders;
import org.junit.Test;
import org.mockito.Matchers;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link bo.gotthardt.jersey.filter.BasicAuthFilter}.
 */
public class BasicAuthFilterTest {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain chain = mock(FilterChain.class);

    @Test
    public void shouldAllowRequestsWithCorrectCredentials() throws IOException, ServletException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(getHeader("testuser", "testpass"));

        new BasicAuthFilter("testuser", "testpass").doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    public void shouldRejectRequestsWithIncorrectCredentials() throws IOException, ServletException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(getHeader("testuser", "WRONGpass"));

        new BasicAuthFilter("testuser", "testpass").doFilter(request, response, chain);

        verify(response).setHeader(Matchers.eq(HttpHeaders.WWW_AUTHENTICATE), Matchers.contains("Basic"));
        verify(response).sendError(401);
    }

    @Test
    public void shouldRejectRequestsWithoutCredentials() throws IOException, ServletException {
        new BasicAuthFilter("testuser", "testpass").doFilter(request, response, chain);

        verify(response).setHeader(Matchers.eq(HttpHeaders.WWW_AUTHENTICATE), Matchers.contains("Basic"));
        verify(response).sendError(401);
    }

    @Test
    public void shouldRejectRequestsWithInvalidHeader() throws IOException, ServletException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("blah");

        new BasicAuthFilter("testuser", "testpass").doFilter(request, response, chain);

        verify(response).setHeader(Matchers.eq(HttpHeaders.WWW_AUTHENTICATE), Matchers.contains("Basic"));
        verify(response).sendError(401);
    }

    private static String getHeader(String username, String password) {
        String credentials = BaseEncoding.base64().encode((username + ":" + password).getBytes());
        return "Basic " + credentials;
    }
}