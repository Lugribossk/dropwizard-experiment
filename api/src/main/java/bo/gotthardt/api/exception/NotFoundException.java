package bo.gotthardt.api.exception;

import org.bson.types.ObjectId;

import javax.ws.rs.core.Response;

/**
 * @author Bo Gotthardt
 */
public class NotFoundException extends JsonMessageException {
    public NotFoundException(ObjectId id) {
        super(Response.Status.NOT_FOUND.getStatusCode(), "No object with id=" + id + " found.");
    }
}
