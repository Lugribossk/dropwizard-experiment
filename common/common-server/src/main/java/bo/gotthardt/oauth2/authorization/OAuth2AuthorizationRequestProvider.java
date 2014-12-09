package bo.gotthardt.oauth2.authorization;

import javax.ws.rs.ext.Provider;

/**
 * Jersey injectable provider that enables {@link OAuth2AccessTokenResource} methods to have {@link OAuth2AuthorizationRequest} parameters.
 *
 * @author Bo Gotthardt
 */
@Provider
public class OAuth2AuthorizationRequestProvider {

    public OAuth2AuthorizationRequest provide() {
        return null;
    }

    /*public OAuth2AuthorizationRequestProvider() {
        super(OAuth2AuthorizationRequest.class);
    }

    @Override
    public OAuth2AuthorizationRequest getValue(HttpContext c) {
        Form formParameters = c.getRequest().getFormParameters();

        String grantType = formParameters.getFirst("grant_type");
        WebAppPreconditions.checkArgumentNotNull(grantType, "OAuth2 authentication request requires a 'grant_type' form parameter.");

        switch (grantType) {
            case "password":
                return passwordFromFormParameters(formParameters);
//            case "authorization_code":
//                throw new WebApplicationException();
//            case "client_credentials":
//                throw new WebApplicationException();
            default:
                throw new JsonMessageException(HttpStatus.NOT_IMPLEMENTED_501, "Grant type '%s' not implemented.", grantType);
        }
    }

    private static OAuth2AuthorizationPasswordRequest passwordFromFormParameters(Form formParameters) {
        Preconditions.checkState("password".equals(formParameters.getFirst("grant_type")));

        String username = formParameters.getFirst("username");
        WebAppPreconditions.checkArgumentNotNull(username, "'Password' grant type requires a 'username' form parameter.");

        String password = formParameters.getFirst("password");
        WebAppPreconditions.checkArgumentNotNull(password, "'Password' grant type requires a 'password' form parameter.");

        return new OAuth2AuthorizationPasswordRequest(username, password);
    }*/


}