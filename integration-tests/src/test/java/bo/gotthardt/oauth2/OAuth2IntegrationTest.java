package bo.gotthardt.oauth2;

import bo.gotthardt.model.OAuth2AccessToken;
import bo.gotthardt.model.User;
import bo.gotthardt.oauth2.authentication.OAuth2Authenticator;
import bo.gotthardt.oauth2.authorization.OAuth2AccessTokenResource;
import bo.gotthardt.oauth2.authorization.OAuth2AuthorizationRequestProvider;
import bo.gotthardt.test.ApiIntegrationTest;
import bo.gotthardt.user.UserResource;
import com.google.common.net.HttpHeaders;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.oauth.OAuthFactory;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * Tests for the OAuth2 functionality working end-to-end.
 *
 * @author Bo Gotthardt
 */
public class OAuth2IntegrationTest extends ApiIntegrationTest {
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new OAuth2AccessTokenResource(db))
            .addResource(new UserResource(db))
            .addResource(new OAuth2AuthorizationRequestProvider())
            .addResource(AuthFactory.binder(new OAuthFactory<>(new OAuth2Authenticator(db), "OAuth2", User.class)))
            .build();

    private User user;

    @Before
    public void setup() {
        user = createUser();
    }

    @Test
    public void shouldCreateAndSendTokenThatIdentifiesUser() {
        Response response = POST("/token", loginParameters("testuser", "testpass"), MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        assertThat(response).hasStatus(Response.Status.OK);

        OAuth2AccessToken token = response.readEntity(OAuth2AccessToken.class);
        // The token sent in the response won't have any user information, but if we get it from the database it will have.
        assertThat(db.find(OAuth2AccessToken.class, token.getAccessToken()).getUser().getId())
                .isEqualTo(user.getId());
    }

    @Test
    public void shouldNotCreateTokenForInvalidCredentials() {
        assertThat(POST("/token", loginParameters("testuser", "WRONGPASSWORD"), MediaType.APPLICATION_FORM_URLENCODED_TYPE))
                .hasStatus(Response.Status.UNAUTHORIZED);

        assertThat(db.find(OAuth2AccessToken.class).findRowCount())
                .isEqualTo(0);
    }

    @Test
    public void shouldNotCreateTokenForNonexistentCredentials() {
        assertThat(POST("/token", loginParameters("DOESNOTEXIST", "testpass"), MediaType.APPLICATION_FORM_URLENCODED_TYPE))
                .hasStatus(Response.Status.UNAUTHORIZED);

        assertThat(db.find(OAuth2AccessToken.class).findRowCount())
                .isEqualTo(0);
    }

    @Test
    public void should400WhenMissingGrantType() {
        assertThat(POST("/token", null))
                .hasStatus(Response.Status.BAD_REQUEST);
    }

    @Test
    public void should400WhenGrantTypePasswordIsMissingUsername() {
        assertThat(POST("/token", loginParameters(null, "testpass"), MediaType.APPLICATION_FORM_URLENCODED_TYPE))
                .hasStatus(Response.Status.BAD_REQUEST);

    }

    @Test
    public void should400WhenGrantTypePasswordIsMissingPassword() {
        assertThat(POST("/token", loginParameters("testuser", null)))
                .hasStatus(Response.Status.BAD_REQUEST);

    }

    @Test
    public void shouldRefuseNonAuthorizedAccessToAuthProtectedResource() {
        assertThat(GET("/users/" + user.getId()))
                .hasStatus(Response.Status.UNAUTHORIZED);
    }

    @Test
    public void shouldRefuseUnauthorizedAccessToAuthProtectedResource() {
        Response response = resources.client().target("/users/" + user.getId()).request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer WRONGTOKEN")
                .get();

        assertThat(response)
                .hasStatus(Response.Status.UNAUTHORIZED);
    }

    @Test
    public void shouldAllowAuthorizedAccessToProtectedResource() {
        OAuth2AccessToken token = POST("/token", loginParameters("testuser", "testpass"), MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .readEntity(OAuth2AccessToken.class);

        Response response = resources.client().target("/users/" + user.getId()).request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .get();

        assertThat(response)
                .hasStatus(Response.Status.OK)
                .hasJsonContent(user);
    }

    @Test
    public void shouldInvalidateTokens() {
        OAuth2AccessToken token = new OAuth2AccessToken(user, Duration.standardHours(1));
        db.save(token);

        Response response = resources.client().target("/token").request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .delete();
        db.refresh(token);

        assertThat(token.isValid()).isFalse();
    }

    private User createUser() {
        User user = new User("testuser", "testpass", "Testuser");
        db.save(user);
        return user;
    }

    private static MultivaluedMap<String, String> loginParameters(String username, String password) {
        MultivaluedMap<String, String> parameters = new MultivaluedStringMap();
        parameters.add("grant_type", "password");
        if (username != null) {
            parameters.add("username", username);
        }
        if (password != null) {
            parameters.add("password", password);
        }
        return parameters;
    }

    @Override
    public ResourceTestRule getResources() {
        return resources;
    }
}
