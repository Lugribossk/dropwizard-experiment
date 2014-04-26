package bo.gotthardt.test;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for API integration tests.
 *
 * @author Bo Gotthardt
 */
public abstract class ApiIntegrationTest {
    protected static final InMemoryEbeanServer db = new InMemoryEbeanServer();

    @Before
    public void clearDatabase() {
        db.clear();
    }

    @Before
    public void squelchSpammyLoggers() {
        Logger.getLogger("com.sun.jersey").setLevel(Level.WARNING);
    }

    public abstract ResourceTestRule getResources();

    protected ClientResponse GET(String path) {
        return getResources().client().resource(path).get(ClientResponse.class);
    }

    protected ClientResponse POST(String path, Object input, MediaType type) {
        return getResources().client().resource(path).entity(input, type).post(ClientResponse.class);
    }

    protected ClientResponse POST(String path, Object input) {
        return POST(path, input, MediaType.APPLICATION_JSON_TYPE);
    }

    protected ClientResponse PUT(String path, Object input, MediaType type) {
        return getResources().client().resource(path).entity(input, type).put(ClientResponse.class);
    }

    protected ClientResponse PUT(String path, Object input) {
        return PUT(path, input,  MediaType.APPLICATION_JSON_TYPE);
    }

    protected ClientResponse DELETE(String path) {
        return getResources().client().resource(path).delete(ClientResponse.class);
    }

    protected static MultivaluedMap<String, String> formParameters(String... keyValues) {
        Preconditions.checkArgument(keyValues.length % 2 == 0, "Must have an even number of arguments.");
        MultivaluedMap<String, String> parameters = new MultivaluedMapImpl();

        for (int i = 0; i < keyValues.length; i = i + 2) {
            parameters.add(keyValues[i], keyValues[i + 1]);
        }

        return parameters;
    }
}
