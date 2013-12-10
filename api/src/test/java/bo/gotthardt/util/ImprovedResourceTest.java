package bo.gotthardt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.client.ClientResponse;
import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Before;

import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bo Gotthardt
 */
public abstract class ImprovedResourceTest extends ResourceTest {
    private final ObjectMapper MAPPER = getObjectMapperFactory().build();

    @Before
    public void squelchSpammyLoggers() {
        Logger.getLogger("com.sun.jersey").setLevel(Level.WARNING);
    }

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

    protected ClientResponse DELETE(String path) {
        return client().resource(path).delete(ClientResponse.class);
    }
}
