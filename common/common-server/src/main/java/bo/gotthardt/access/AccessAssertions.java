package bo.gotthardt.access;

import bo.gotthardt.exception.UnauthorizedException;
import bo.gotthardt.model.Customer;
import bo.gotthardt.model.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for Owned and Principal assertions, since default methods cannot have a static logger.
 */
@Slf4j
class AccessAssertions {
    static void assertOwnedBy(Owned owned, Customer customer) {
        if (!owned.isOwnedBy(customer)) {
            log.info("Denied access to {} for {}.", owned, customer);
            throw new UnauthorizedException();
        }
    }

    static void assertOwnedBy(Owned owned, User user) {
        if (!owned.isOwnedBy(user)) {
            log.info("Denied access to {} for {}.", owned, user);
            throw new UnauthorizedException();
        }
    }

    static void assertAccessTo(Principal principal, Feature feature) {
        if (!principal.hasAccessTo(feature)) {
            log.info("Denied access to {} for {}.", feature, principal);
            throw new UnauthorizedException();
        }
    }

}
