package bo.gotthardt.api.exception;

import javax.ws.rs.core.Response;

/**
 * @author Bo Gotthardt
 */
public class NotFoundException extends JsonMessageException {
    public NotFoundException(long id) {
        super(Response.Status.NOT_FOUND, "No object with id=" + id + " found.");
    }
}
