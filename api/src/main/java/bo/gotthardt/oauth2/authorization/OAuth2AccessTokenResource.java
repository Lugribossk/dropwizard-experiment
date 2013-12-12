package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.api.exception.UnauthorizedException;
import bo.gotthardt.model.OAuth2AccessToken;
import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import io.dropwizard.auth.Auth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Jersey resource that provides {@link OAuth2AccessToken}s in response to {@link OAuth2AuthorizationRequest}s.
 * <br/><br/>
 * Requires the {@link OAuth2AuthorizationRequestProvider} to be present.
 *
 * @author Bo Gotthardt
 */
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@Path("/token")
public class OAuth2AccessTokenResource {
    private static final Duration TOKEN_LIFETIME = Duration.standardDays(365);

    private final EbeanServer ebean;

    @POST
    public OAuth2AccessToken token(@Context OAuth2AuthorizationRequest authRequest) {
        Optional<User> user = authRequest.getValidUser(ebean);
        if (!user.isPresent()) {
            throw new UnauthorizedException();
        }

        OAuth2AccessToken token = new OAuth2AccessToken(user.get(), TOKEN_LIFETIME);
        ebean.save(token);
        log.info("Created token %s", token);

        return token;
    }

    @DELETE
    public void delete(@Auth User user, @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        // Since the user was authenticated, we know that the header is present, valid and points to an access token owned by the user.
        String accessToken = authHeader.substring(authHeader.indexOf(" ") + 1);
        OAuth2AccessToken token = ebean.find(OAuth2AccessToken.class, accessToken);

        Preconditions.checkNotNull(token);
        Preconditions.checkState(user.getId() == token.getUser().getId());

        token.setExpirationDate(DateTime.now());
        ebean.save(token);
        log.info("Expired token %s", token);
    }
}
