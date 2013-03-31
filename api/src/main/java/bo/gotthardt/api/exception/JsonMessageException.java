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

    public JsonMessageException(int status, String message, Object... args) {
        super(createResponse(status, message, args));
    }

    private static Response createResponse(int status, String message, Object... args) {
        ObjectNode output = OBJECT_MAPPER.createObjectNode();
        output.put("status", status);
        output.put("message", String.format(message, args));

        return Response.status(status)
                .entity(output)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
