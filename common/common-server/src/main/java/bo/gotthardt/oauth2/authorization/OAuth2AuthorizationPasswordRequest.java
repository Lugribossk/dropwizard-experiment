package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.exception.WebAppPreconditions;
import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.core.MultivaluedMap;

/**
 * An OAuth2 "password" grant type authorization request. Also known as "username-password flow".
 *
 * @author Bo Gotthardt
 */
@RequiredArgsConstructor
public class OAuth2AuthorizationPasswordRequest implements OAuth2AuthorizationRequest {
    private final String username;
    private final String password;

    @Override
    public Optional<User> getValidUser(EbeanServer ebean) {
        User user = ebean.find(User.class).where().eq("username", username).findUnique();

        if (user != null && user.getPassword().equalsPlaintext(password)) {
            return Optional.of(user);
        } else {
            return Optional.absent();
        }
    }

    public static OAuth2AuthorizationPasswordRequest fromQueryParameters(MultivaluedMap<String, String> queryParameters) {
        Preconditions.checkState("password".equals(queryParameters.getFirst("grant_type")));

        String username = queryParameters.getFirst("username");
        WebAppPreconditions.checkArgumentNotNull(username, "'Password' grant type requires a 'username' query parameter.");

        String password = queryParameters.getFirst("password");
        WebAppPreconditions.checkArgumentNotNull(password, "'Password' grant type requires a 'password' query parameter.");

        return new OAuth2AuthorizationPasswordRequest(username, password);
    }
}
