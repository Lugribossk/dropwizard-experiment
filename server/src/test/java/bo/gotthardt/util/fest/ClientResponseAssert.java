package bo.gotthardt.util.fest;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.jersey.api.client.ClientResponse;
import com.yammer.dropwizard.json.ObjectMapperFactory;
import org.fest.assertions.api.AbstractAssert;
import org.junit.ComparisonFailure;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Bo Gotthardt
 */
public class ClientResponseAssert extends AbstractAssert<ClientResponseAssert, ClientResponse> {
    private static final ObjectMapper MAPPER;

    static {
        ObjectMapperFactory factory = new ObjectMapperFactory();
        factory.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        factory.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        factory.enable(SerializationFeature.INDENT_OUTPUT);
        MAPPER = factory.build();
    }

    public ClientResponseAssert(ClientResponse actual) {
        super(actual, ClientResponseAssert.class);
    }

    public ClientResponseAssert hasStatus(Response.StatusType status) {
        isNotNull();

        compare(String.valueOf(actual.getStatus()), String.valueOf(status.getStatusCode()), "Status");

        return this;
    }

    public ClientResponseAssert hasContentType(MediaType type) {
        isNotNull();

        compare(actual.getType().getType(), type.getType(), "Content type");

        return this;
    }

    public <T> ClientResponseAssert hasJsonContent(T expected) {
        isNotNull();
        hasContentType(MediaType.APPLICATION_JSON_TYPE);

        try {
            // Should probably be something with getEntity(new GenericType<blah>() {}) but that doesn't work and this does...
            String actualJson = MAPPER.writeValueAsString(actual.getEntity(Object.class));
            String expectedJson = MAPPER.writeValueAsString(expected);
            compare(actualJson, expectedJson, "JSON content");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    /**
     * Compare the specified actual and expected values, and throw a ComparisonFailure if they are not equal.
     * The type of the values are Strings to allow us to use ComparisonFailure for IDE integration.
     *
     * @param actual the actual value
     * @param expected the expected value
     * @param message the message to show on failure
     */
    private static void compare(String actual, String expected, String message) {
        if (actual == null || !actual.equals(expected)) {
            throw new ComparisonFailure(message, expected, actual);
        }
    }
}
