package bo.gotthardt.oauth2;

import bo.gotthardt.ebean.EbeanBundle;
import bo.gotthardt.oauth2.authentication.OAuth2Authenticator;
import bo.gotthardt.oauth2.authorization.OAuth2AccessTokenResource;
import bo.gotthardt.oauth2.authorization.OAuth2AuthorizationRequestProvider;
import com.avaje.ebean.EbeanServer;
import io.dropwizard.Bundle;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.oauth.OAuthProvider;
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
        EbeanServer db = ebeanBundle.getEbeanServer();
        environment.jersey().register(new OAuth2AccessTokenResource(db));
        environment.jersey().register(new OAuth2AuthorizationRequestProvider());

        environment.jersey().register(new OAuthProvider<>(new OAuth2Authenticator(db), "realm"));
    }
}
