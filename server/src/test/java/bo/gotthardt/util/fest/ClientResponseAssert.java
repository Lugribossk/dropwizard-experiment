package bo.gotthardt.util.fest;

import com.sun.jersey.api.client.ClientResponse;
import com.yammer.dropwizard.testing.JsonHelpers;
import org.fest.assertions.api.AbstractAssert;
import org.junit.ComparisonFailure;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Bo Gotthardt
 */
public class ClientResponseAssert extends AbstractAssert<ClientResponseAssert, ClientResponse> {
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
            String actualJson = JsonHelpers.asJson(actual.getEntity(Object.class));
            String expectedJson = JsonHelpers.asJson(expected);
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
