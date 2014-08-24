package bo.gotthardt.jersey.filter;

import com.google.common.io.BaseEncoding;
import com.google.common.net.HttpHeaders;
import io.dropwizard.setup.Environment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Response;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;

/**
 * Servlet filter that does HTTP Basic authentication.
 * Intended for use with the admin port.
 *
 * @author Bo Gotthardt
 */
@Slf4j
@RequiredArgsConstructor
public class BasicAuthFilter implements Filter {
    private final String username;
    private final String password;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Empty on purpose.
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Basic ")) {
            String decoded = new String(BaseEncoding.base64().decode(header.substring(header.indexOf(" ") + 1)));

            if (decoded.contains(":")) {
                String username = decoded.substring(0, decoded.indexOf(":"));
                String password = decoded.substring(decoded.indexOf(":") + 1, decoded.length());

                if (username.equals(this.username) && password.equals(this.password)) {
                    chain.doFilter(request, response);
                    return;
                } else {
                    log.info("Incorrect admin login with username '{}'.", username);
                }
            }
        }

        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Administration\"");
        response.sendError(Response.SC_UNAUTHORIZED);
    }

    @Override
    public void destroy() {
        // Empty on purpose.
    }

    public static void addToAdmin(Environment env, String username, String password) {
        env.admin().addFilter("auth", new BasicAuthFilter(username, password))
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
