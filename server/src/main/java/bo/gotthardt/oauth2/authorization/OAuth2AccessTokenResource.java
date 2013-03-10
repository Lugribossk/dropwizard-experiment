package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.api.exception.UnauthorizedException;
import bo.gotthardt.model.User;
import bo.gotthardt.oauth2.OAuth2AccessToken;
import com.avaje.ebean.EbeanServer;
import com.google.common.base.Optional;
import lombok.RequiredArgsConstructor;
import org.joda.time.Duration;

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

        return token;
    }
}
