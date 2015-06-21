package bo.gotthardt.oauth2;

import bo.gotthardt.ebean.EbeanBundle;
import bo.gotthardt.model.User;
import bo.gotthardt.oauth2.authentication.OAuth2Authenticator;
import bo.gotthardt.oauth2.authorization.OAuth2AccessTokenResource;
import bo.gotthardt.oauth2.authorization.OAuth2AuthorizationRequestFactory;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.oauth.OAuthFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.RequiredArgsConstructor;

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

        environment.jersey().register(
            AuthFactory.binder(new OAuthFactory<>(new OAuth2Authenticator(ebeanBundle.getEbeanServer()), "OAuth2", User.class)));
    }
}
