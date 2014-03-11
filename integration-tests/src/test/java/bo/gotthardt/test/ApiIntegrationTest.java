package bo.gotthardt.test;

import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;

import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bo Gotthardt
 */
public abstract class ApiIntegrationTest {
    protected static final InMemoryEbeanServer ebean = new InMemoryEbeanServer();

    @Before
    public void clearDatabase() {
        ebean.clear();
    }

    @Before
    public void squelchSpammyLoggers() {
        Logger.getLogger("com.sun.jersey").setLevel(Level.WARNING);
    }

    public abstract ResourceTestRule getResources();

    public ClientResponse GET(String path) {
        return getResources().client().resource(path).get(ClientResponse.class);
    }

    public ClientResponse POST(String path, Object input, MediaType type) {
        return getResources().client().resource(path).entity(input, type).post(ClientResponse.class);
    }

    public ClientResponse POST(String path, Object input) {
        return POST(path, input, MediaType.APPLICATION_JSON_TYPE);
    }

    public ClientResponse PUT(String path, Object input, MediaType type) {
        return getResources().client().resource(path).entity(input, type).put(ClientResponse.class);
    }

    public ClientResponse PUT(String path, Object input) {
        return PUT(path, input,  MediaType.APPLICATION_JSON_TYPE);
    }

    public ClientResponse DELETE(String path) {
        return getResources().client().resource(path).delete(ClientResponse.class);
    }
}
