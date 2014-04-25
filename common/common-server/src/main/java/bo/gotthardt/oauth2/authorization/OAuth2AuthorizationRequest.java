package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import com.google.common.base.Optional;

/**
 * An OAuth2 authorization request for an {@link bo.gotthardt.model.OAuth2AccessToken}.
 *
 * @author Bo Gotthardt
 */
public interface OAuth2AuthorizationRequest {
    Optional<User> getValidUser(EbeanServer db);
}
