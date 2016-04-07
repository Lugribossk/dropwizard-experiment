package bo.gotthardt.oauth2;

import bo.gotthardt.ebean.EbeanBundle;
import bo.gotthardt.model.User;
import bo.gotthardt.oauth2.authentication.UserAuthenticator;
import bo.gotthardt.oauth2.authorization.OAuth2AccessTokenResource;
import bo.gotthardt.oauth2.authorization.OAuth2AuthorizationRequestFactory;
import bo.gotthardt.oauth2.authorization.UserAuthorizer;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.RequiredArgsConstructor;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Dropwizard bundle with OAuth2 functionality.
 *
 * @author Bo Gotthardt
 */
@RequiredArgsConstructor
public class OAuth2Bundle implements ConfiguredBundle<Object> { // Should really be just a Bundle, but that messes with the initialization order.
    private final EbeanBundle ebeanBundle;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // Do nothing.
    }

    @Override
    public void run(Object configuration, Environment environment) throws Exception {
        environment.jersey().register(OAuth2AccessTokenResource.class);
        environment.jersey().register(OAuth2AuthorizationRequestFactory.getBinder());

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(new AuthDynamicFeature(
            new OAuthCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new UserAuthenticator(ebeanBundle.getEbeanServer()))
                .setAuthorizer(new UserAuthorizer())
                .setPrefix("Bearer")
                .buildAuthFilter()));
    }
}
