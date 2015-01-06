package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.exception.JsonMessageException;
import bo.gotthardt.exception.WebAppPreconditions;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.ws.rs.FormParam;

/**
 * Jersey factory that enables {@link OAuth2AccessTokenResource} methods to have {@link OAuth2AuthorizationRequest} parameters.
 *
 * @author Bo Gotthardt
 */
public class OAuth2AuthorizationRequestFactory implements Factory<OAuth2AuthorizationRequest> {
    @FormParam("grant_type")
    private String grantType;

    @FormParam("username")
    private String username;

    @FormParam("password")
    private String password;

    public OAuth2AuthorizationRequest provide() {
        try {
            WebAppPreconditions.checkArgumentNotNull(grantType, "OAuth2 authentication request requires a 'grant_type' form parameter.");

            switch (grantType) {
                case "password":
                    WebAppPreconditions.checkArgumentNotNull(username, "'Password' grant type requires a 'username' form parameter.");
                    WebAppPreconditions.checkArgumentNotNull(password, "'Password' grant type requires a 'password' form parameter.");
                    return new OAuth2AuthorizationPasswordRequest(username, password);
//            case "authorization_code":
//                throw new WebApplicationException();
//            case "client_credentials":
//                throw new WebApplicationException();
                default:
                    throw new JsonMessageException(HttpStatus.NOT_IMPLEMENTED_501, "Grant type '%s' not implemented.", grantType);
            }
        } catch (RuntimeException e) {
            // Jersey 2 does not support throwing exceptions in parameter injection providers (unlike Jersey 1).
            // So instead return an anonymous subclass where the functional interface method throws the exception...
            return db -> {
                throw e;
            };
        }
    }

    @Override
    public void dispose(OAuth2AuthorizationRequest instance) {
        // Empty on purpose.
    }

    public static Binder getBinder() {
        return new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(OAuth2AuthorizationRequestFactory.class).to(OAuth2AuthorizationRequest.class);
            }
        };
    }
}