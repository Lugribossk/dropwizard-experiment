package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.exception.JsonMessageException;
import bo.gotthardt.exception.UnauthorizedException;
import bo.gotthardt.model.OAuth2AccessToken;
import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.net.HttpHeaders;
import com.google.common.util.concurrent.RateLimiter;
import com.sun.xml.internal.ws.api.message.Packet;
import io.dropwizard.auth.Auth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    private static final LoadingCache<String, RateLimiter> limiters = buildCache();

    private final EbeanServer db;

    @POST
    public OAuth2AccessToken token(@Context OAuth2AuthorizationRequest authRequest) {
        RateLimiter limiter = limiters.getUnchecked(authRequest.getCacheKey());
        if (!limiter.tryAcquire()) {
            log.warn("Request limit exceeded for {}", authRequest.getCacheKey());
            throw new JsonMessageException(429, "Too many requests");
        }

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
        Preconditions.checkState(user.getId() == token.getUser().getId());

        token.setExpirationDate(DateTime.now());
        db.save(token);
        log.info("Expired token %s", token);
    }

    private static LoadingCache<String, RateLimiter> buildCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(new CacheLoader<String, RateLimiter>() {
                    @Override
                    public RateLimiter load(@Nonnull String key) throws Exception {
                        return RateLimiter.create(1.0);
                    }
                });
    }
}
