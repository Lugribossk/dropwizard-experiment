package bo.gotthardt.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.ResourceTestRule;

import javax.ws.rs.core.MediaType;

/**
 * @author Bo Gotthardt
 */
public class RestHelper {
    private final Client client;

    public RestHelper(ResourceTestRule rule) {
        this.client = rule.client();
    }

    public ClientResponse GET(String path) {
        return client.resource(path).get(ClientResponse.class);
    }

    public ClientResponse POST(String path, Object input, MediaType type) {
        return client.resource(path).entity(input, type).post(ClientResponse.class);
    }

    public ClientResponse POST(String path, Object input) {
        return POST(path, input, MediaType.APPLICATION_JSON_TYPE);
    }

    public ClientResponse PUT(String path, Object input, MediaType type) {
        return client.resource(path).entity(input, type).put(ClientResponse.class);
    }

    public ClientResponse PUT(String path, Object input) {
        return PUT(path, input,  MediaType.APPLICATION_JSON_TYPE);
    }

    public ClientResponse DELETE(String path) {
        return client.resource(path).delete(ClientResponse.class);
    }
}
