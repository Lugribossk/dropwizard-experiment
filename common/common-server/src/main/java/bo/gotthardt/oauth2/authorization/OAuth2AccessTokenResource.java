package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.exception.UnauthorizedException;
import bo.gotthardt.model.OAuth2AccessToken;
import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import io.dropwizard.auth.Auth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Jersey resource that provides {@link OAuth2AccessToken}s in response to {@link OAuth2AuthorizationRequest}s.
 * <br/><br/>
 * Requires the {@link OAuth2AuthorizationRequestFactory} to be present.
 *
 * @author Bo Gotthardt
 */
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@Path("/token")
public class OAuth2AccessTokenResource {
    private static final Duration TOKEN_LIFETIME = Duration.ofDays(365);

    private final EbeanServer db;

    @POST
    public OAuth2AccessToken token(@Context OAuth2AuthorizationRequest authRequest) {
        Optional<User> user = authRequest.getValidUser(db);
        if (!user.isPresent()) {
            throw new UnauthorizedException();
        }

        OAuth2AccessToken token = new OAuth2AccessToken(user.get(), TOKEN_LIFETIME);
        db.save(token);
        log.info("Created token for {}", user.get());

        return token;
    }

    @DELETE
    public void delete(@Auth User user, @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        // Since the user was authenticated, we know that the header is present, valid and points to an access token owned by the user.
        String accessToken = authHeader.substring(authHeader.indexOf(" ") + 1);
        OAuth2AccessToken token = db.find(OAuth2AccessToken.class, accessToken);

        Preconditions.checkNotNull(token);
        Preconditions.checkState(user.getId().equals(token.getUser().getId()));

        token.setExpirationDate(LocalDateTime.now());
        db.save(token);
        log.info("Expired token %s", token);
    }
}
