package bo.gotthardt.rest;

import com.google.common.net.HttpHeaders;
import io.dropwizard.jetty.setup.ServletEnvironment;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;

public class CrossOriginResourceSharing implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing.
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // "*" is the only safe setting, attempting to mirror back the request origin does not work with localhost and can lead to problems with caching.
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,PUT,POST,DELETE,OPTIONS");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "172800"); // 24 hours, most browsers cache it for less than this anyway.

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Do nothing.
    }

    public static void enableFor(ServletEnvironment servlets, String path) {
        servlets.addFilter("blah", CrossOriginResourceSharing.class)
            .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, path);
    }
}