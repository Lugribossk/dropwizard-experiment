package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import com.google.common.base.Optional;
import lombok.RequiredArgsConstructor;

/**
 * An OAuth2 "password" grant type authorization request. Also known as "username-password flow" or "resource owner credentials grant".
 *
 * @author Bo Gotthardt
 */
@RequiredArgsConstructor
public class OAuth2AuthorizationPasswordRequest implements OAuth2AuthorizationRequest {
    private final String username;
    private final String password;

    @Override
    public Optional<User> getValidUser(EbeanServer db) {
        User user = db.find(User.class).where().eq("username", username).findUnique();

        if (user != null && user.getPassword().equalsPlaintext(password)) {
            return Optional.of(user);
        } else {
            return Optional.absent();
        }
    }

    @Override
    public String getCacheKey() {
        return password;
    }
}
