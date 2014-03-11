package bo.gotthardt.exception;

import bo.gotthardt.AccessibleBy;
import bo.gotthardt.Principal;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response;

/**
 * @author Bo Gotthardt
 */
@Slf4j
public class WebAppPreconditions {

    public static void checkArgumentNotNull(@Nullable Object object, String message, Object... args) {
        if (object == null) {
            throw new JsonMessageException(Response.Status.BAD_REQUEST.getStatusCode(), message, args);
        }
    }

    public static <P extends Principal, A extends AccessibleBy<P>> void assertAccessTo(P principal, A item) {
        if (!item.isAccessibleBy(principal)) {
            log.info("Denied access to {} for {}.", item, principal);
            throw new UnauthorizedException();
        }
    }
}
