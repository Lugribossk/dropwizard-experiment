package bo.gotthardt.oauth2.authentication;

import bo.gotthardt.model.OAuth2AccessToken;
import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import com.google.common.base.Optional;
import io.dropwizard.auth.Authenticator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Dropwizard {@link Authenticator} that authenticates {@link OAuth2AccessToken}s into {@link User}s.
 * <br/><br/>
 * The tokens are provided as the String value extracted from the Authorization header.
 *
 * @author Bo Gotthardt
 */
@Slf4j
@RequiredArgsConstructor
public class OAuth2Authenticator implements Authenticator<String, User> {
    private final EbeanServer db;

    @Override
    public Optional<User> authenticate(String credentials) {
        OAuth2AccessToken token = db.find(OAuth2AccessToken.class, credentials);

        if (token == null) {
            log.info("Access token '{}' not found.", credentials);
            return Optional.absent();
        }

        if (!token.isValid()) {
            log.info("Access token '{}' is no longer valid, expired at {}.", credentials, token.getExpirationDate());
            return Optional.absent();
        }

        User user = token.getUser();
        log.info("Authenticated user {} with access token '{}'.", user, token.loggableAccessToken());
        return Optional.of(user);
    }
}
