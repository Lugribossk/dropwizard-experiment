package bo.gotthardt.test;

import com.google.common.base.Preconditions;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Before;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

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
        //Logger.getLogger("com.sun.jersey").setLevel(Level.WARNING);
    }

    public abstract ResourceTestRule getResources();

    protected Response GET(String path) {
        return getResources().client().target(path).request().get();
    }

    protected Response POST(String path, Object input, MediaType type) {
        return getResources().client().target(path).request().post(Entity.entity(input, type));
    }

    protected Response POST(String path, Object input) {
        return POST(path, input, MediaType.APPLICATION_JSON_TYPE);
    }

    protected Response PUT(String path, Object input, MediaType type) {
        return getResources().client().target(path).request().put(Entity.entity(input, type));
    }

    protected Response PUT(String path, Object input) {
        return PUT(path, input,  MediaType.APPLICATION_JSON_TYPE);
    }

    protected Response DELETE(String path) {
        return getResources().client().target(path).request().delete();
    }

    protected static MultivaluedMap<String, String> formParameters(String... keyValues) {
        Preconditions.checkArgument(keyValues.length % 2 == 0, "Must have an even number of arguments.");
        MultivaluedMap<String, String> parameters = new MultivaluedStringMap();

        for (int i = 0; i < keyValues.length; i = i + 2) {
            parameters.add(keyValues[i], keyValues[i + 1]);
        }

        return parameters;
    }
}
