package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.jersey.provider.AbstractInjectableProvider;
import com.sun.jersey.api.core.HttpContext;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 * Jetty injectable provider that enables {@link OAuth2AccessTokenResource} methods to have {@link OAuth2AuthorizationRequest} parameters.
 *
 * @author Bo Gotthardt
 */
@Provider
public class OAuth2AuthorizationRequestProvider extends AbstractInjectableProvider<Context, OAuth2AuthorizationRequest> {

    public OAuth2AuthorizationRequestProvider() {
        super(OAuth2AuthorizationRequest.class);
    }

    @Override
    public OAuth2AuthorizationRequest getValue(HttpContext c) {
        MultivaluedMap<String, String> queryParameters = c.getRequest().getQueryParameters();

        String grantType = queryParameters.getFirst("grant_type");
        required(grantType, "OAuth2 authentication request requires a 'grant_type' query parameter.");

        switch (grantType) {
            case "authorization_code":
                return createAuthorizationCodeRequest(queryParameters);
            case "password":
                return createPasswordRequest(queryParameters);
            case "client_credentials":
                return createClientCredentialsRequest(queryParameters);
            default:
                throw new WebApplicationException();
        }
    }

    private static OAuth2AuthorizationPasswordRequest createPasswordRequest(MultivaluedMap<String, String> queryParameters) {
        String username = queryParameters.getFirst("username");
        required(username, "Password grant type requires a 'username' query parameter.");

        String password = queryParameters.getFirst("password");
        required(password, "Password grant type requires a 'password' query parameter.");

        return new OAuth2AuthorizationPasswordRequest(username, password);
    }

    private static OAuth2AuthorizationRequest createAuthorizationCodeRequest(MultivaluedMap<String, String> queryParameters) {
        throw new WebApplicationException();
    }

    private static OAuth2AuthorizationRequest createClientCredentialsRequest(MultivaluedMap<String, String> queryParameters) {
        throw new WebApplicationException();
    }

    private static void required(Object blah, String message) {
        if (blah == null) {
            // TODO
            throw new WebApplicationException();
        }
    }
}