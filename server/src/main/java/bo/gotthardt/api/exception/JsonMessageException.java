package bo.gotthardt.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Bo Gotthardt
 */
public class JsonMessageException extends WebApplicationException {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public JsonMessageException(Response.Status status, String message) {
        super(createResponse(status, message));
    }

    private static Response createResponse(Response.Status status, String message) {
        ObjectNode output = OBJECT_MAPPER.createObjectNode();
        output.put("status", status.getStatusCode());
        output.put("message", message);

        return Response.status(status.getStatusCode())
                .entity(output)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
