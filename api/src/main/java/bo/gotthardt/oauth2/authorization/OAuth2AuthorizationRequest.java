package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.model.User;
import com.google.code.morphia.Datastore;
import com.google.common.base.Optional;

/**
 * An OAuth2 authorization request for an {@link bo.gotthardt.model.OAuth2AccessToken}.
 *
 * @author Bo Gotthardt
 */
public interface OAuth2AuthorizationRequest {
    Optional<User> getValidUser(Datastore ds);
}
