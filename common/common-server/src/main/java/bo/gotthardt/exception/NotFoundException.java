package bo.gotthardt.exception;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * @author Bo Gotthardt
 */
public class NotFoundException extends JsonMessageException {
    public NotFoundException(UUID id) {
        super(Response.Status.NOT_FOUND.getStatusCode(), "No object with id=" + id + " found.");
    }
}
