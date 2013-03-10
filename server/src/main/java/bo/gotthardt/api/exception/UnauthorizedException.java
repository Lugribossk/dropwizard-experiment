package bo.gotthardt.api.exception;

import javax.ws.rs.core.Response;

/**
 * @author Bo Gotthardt
 */
public class UnauthorizedException extends JsonMessageException {
    public UnauthorizedException() {
        super(Response.Status.UNAUTHORIZED.getStatusCode(), "Credentials are required to access this resource.");
    }
}
