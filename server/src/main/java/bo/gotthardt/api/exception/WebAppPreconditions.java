package bo.gotthardt.api.exception;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response;

/**
 * @author Bo Gotthardt
 */
public class WebAppPreconditions {

    public static void checkArgumentNotNull(@Nullable Object object, String message, Object... args) {
        if (object == null) {
            throw new JsonMessageException(Response.Status.BAD_REQUEST.getStatusCode(), message, args);
        }
    }
}
