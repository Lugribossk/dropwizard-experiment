package bo.gotthardt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.client.ClientResponse;

import javax.ws.rs.core.MediaType;

/**
 *
 * @author Bo Gotthardt
 */
public abstract class EasierJerseyTest extends EbeanInMemoryTest {
    private final ObjectMapper MAPPER = getObjectMapperFactory().build();

    protected ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    protected ClientResponse GET(String path) {
        return client().resource(path).get(ClientResponse.class);
    }

    protected ClientResponse POST(String path, Object input, MediaType type) {
        return client().resource(path).entity(input, type).post(ClientResponse.class);
    }

    protected ClientResponse POST(String path, Object input) {
        return POST(path, input, MediaType.APPLICATION_JSON_TYPE);
    }

    protected ClientResponse PUT(String path, Object input, MediaType type) {
        return client().resource(path).entity(input, type).put(ClientResponse.class);
    }

    protected ClientResponse PUT(String path, Object input) {
        return PUT(path, input,  MediaType.APPLICATION_JSON_TYPE);
    }
}
