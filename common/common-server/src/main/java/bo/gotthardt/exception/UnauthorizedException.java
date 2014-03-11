package bo.gotthardt.exception;

import javax.ws.rs.core.Response;

/**
 * @author Bo Gotthardt
 */
public class UnauthorizedException extends JsonMessageException {
    public UnauthorizedException() {
        // TODO Add WWW-Authenticate: Bearer header
        super(Response.Status.UNAUTHORIZED.getStatusCode(), "Credentials are required to access this resource.");
    }
}
