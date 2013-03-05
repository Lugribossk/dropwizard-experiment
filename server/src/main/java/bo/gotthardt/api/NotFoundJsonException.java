package bo.gotthardt.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.Responses;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Bo Gotthardt
 */
public class NotFoundJsonException extends WebApplicationException {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public NotFoundJsonException(long id) {
        super(createResponse(id));
    }

    private static Response createResponse(long id) {
        ObjectNode output = OBJECT_MAPPER.createObjectNode();
        output.put("status", Responses.NOT_FOUND);
        output.put("message", "No object with id=" + id + " found.");

        return Response.status(Responses.NOT_FOUND)
                .entity(output)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
