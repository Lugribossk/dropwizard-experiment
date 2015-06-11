package bo.gotthardt.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.common.base.Preconditions;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.zapodot.jackson.java8.JavaOptionalModule;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
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

    public ResourceTestRule getResources() {
        return null;
    }

    protected WebTarget target(String path) {
        // The base URL does not get set properly when using GrizzlyWebTestContainer, so set it explicitly.
        // Unfortunately it does not seem possible to get the actual port, so use the default port and hope that's the one being used.
        return getResources().client().target("http://localhost:" + TestProperties.DEFAULT_CONTAINER_PORT + path);
    }
    
    protected Response GET(String path) {
        return target(path).request().get();
    }

    protected Response POST(String path, Object input, MediaType type) {
        return target(path).request().post(Entity.entity(input, type));
    }

    protected Response POST(String path, MultivaluedMap input) {
        return POST(path, input, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
    }

    protected Response POST(String path, Object input) {
        return POST(path, input, MediaType.APPLICATION_JSON_TYPE);
    }

    protected Response PUT(String path, Object input, MediaType type) {
        return target(path).request().put(Entity.entity(input, type));
    }

    protected Response PUT(String path, Object input) {
        return PUT(path, input,  MediaType.APPLICATION_JSON_TYPE);
    }

    protected Response DELETE(String path) {
        return target(path).request().delete();
    }

    protected static ObjectMapper getMapper() {
        return Jackson.newObjectMapper()
            .registerModule(new JavaOptionalModule())
            .registerModule(new JSR310Module())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
