package bo.gotthardt.oauth2;

import bo.gotthardt.oauth2.authorization.OAuth2AccessTokenResource;
import bo.gotthardt.model.User;
import bo.gotthardt.oauth2.authentication.OAuth2Authenticator;
import bo.gotthardt.oauth2.authorization.OAuth2AuthorizationRequestProvider;
import com.avaje.ebean.EbeanServer;
import com.yammer.dropwizard.Bundle;
import com.yammer.dropwizard.auth.oauth.OAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
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
        environment.addResource(new OAuth2AccessTokenResource(ebean));
        environment.addProvider(OAuth2AuthorizationRequestProvider.class);

        environment.addProvider(new OAuthProvider<User>(new OAuth2Authenticator(ebean), "realm"));
    }
}
