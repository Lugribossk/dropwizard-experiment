package bo.gotthardt.test.assertj;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import io.dropwizard.jackson.Jackson;
import org.zapodot.jackson.java8.JavaOptionalModule;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class ResponseAssert extends BaseAssert<ResponseAssert, Response> {
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = Jackson.newObjectMapper();
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        MAPPER.registerModule(new JavaOptionalModule());
        MAPPER.registerModule(new JSR310Module());
    }

    public ResponseAssert(Response actual) {
        super(actual, ResponseAssert.class);
    }

    public ResponseAssert hasStatus(Response.StatusType status) {
        isNotNull();

        compare(String.valueOf(actual.getStatus()), String.valueOf(status.getStatusCode()), "Status");

        return this;
    }

    public ResponseAssert hasContentType(MediaType type) {
        return hasContentType(type.getType());
    }

    public ResponseAssert hasContentType(com.google.common.net.MediaType type) {
        return hasContentType(type.type());
    }

    public ResponseAssert hasContentType(String type) {
        isNotNull();

        assertThat(actual.getMediaType()).isNotNull();
        compare(actual.getMediaType().getType(), type, "Content type");

        return this;
    }

    public <T> ResponseAssert hasJsonContent(T expected) {
        isNotNull();
        hasContentType(MediaType.APPLICATION_JSON_TYPE);

        try {
            // Should probably be something with readEntity(new GenericType<blah>() {}) but that doesn't work and this does...
            Object entity = actual.readEntity(Object.class);
            String actualJson = MAPPER.writeValueAsString(entity);
            String expectedJson = MAPPER.writeValueAsString(expected);
            compare(actualJson, expectedJson, "JSON content");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }
}
