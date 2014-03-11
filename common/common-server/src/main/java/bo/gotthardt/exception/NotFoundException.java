package bo.gotthardt.exception;

import javax.ws.rs.core.Response;

/**
 * @author Bo Gotthardt
 */
public class NotFoundException extends JsonMessageException {
    public NotFoundException(long id) {
        super(Response.Status.NOT_FOUND.getStatusCode(), "No object with id=" + id + " found.");
    }
}
