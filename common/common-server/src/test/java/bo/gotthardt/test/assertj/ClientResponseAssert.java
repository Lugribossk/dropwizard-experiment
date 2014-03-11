package bo.gotthardt.test.assertj;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.jackson.Jackson;
import org.assertj.core.api.AbstractAssert;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class ClientResponseAssert extends AbstractAssert<ClientResponseAssert, ClientResponse> {
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = Jackson.newObjectMapper();
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
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

        assertThat(actual.getType()).isNotNull();
        compare(actual.getType().getType(), type.getType(), "Content type");

        return this;
    }

    public <T> ClientResponseAssert hasJsonContent(T expected) {
        isNotNull();
        hasContentType(MediaType.APPLICATION_JSON_TYPE);

        try {
            // Should probably be something with getEntity(new GenericType<blah>() {}) but that doesn't work and this does...
            Object entity = actual.getEntity(Object.class);
            String actualJson = MAPPER.writeValueAsString(entity);
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
            throw new junit.framework.ComparisonFailure(message, expected, actual);
        }
    }
}
