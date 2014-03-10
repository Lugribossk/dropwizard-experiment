package bo.gotthardt.oauth2;

import bo.gotthardt.oauth2.authentication.OAuth2Authenticator;
import bo.gotthardt.oauth2.authorization.OAuth2AccessTokenResource;
import bo.gotthardt.oauth2.authorization.OAuth2AuthorizationRequestProvider;
import com.avaje.ebean.EbeanServer;
import io.dropwizard.Bundle;
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
public class OAuth2Bundle implements Bundle {
    private final EbeanServer ebean;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // Do nothing.
    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(new OAuth2AccessTokenResource(ebean));
        environment.jersey().register(new OAuth2AuthorizationRequestProvider());

        environment.jersey().register(new OAuthProvider<>(new OAuth2Authenticator(ebean), "realm"));
    }
}
