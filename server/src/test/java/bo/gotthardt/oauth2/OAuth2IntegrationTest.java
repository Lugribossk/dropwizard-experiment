package bo.gotthardt.oauth2;

import bo.gotthardt.api.UserResource;
import bo.gotthardt.model.User;
import bo.gotthardt.oauth2.authentication.OAuth2Authenticator;
import bo.gotthardt.oauth2.authorization.OAuth2AccessTokenResource;
import bo.gotthardt.oauth2.authorization.OAuth2AuthorizationRequestProvider;
import bo.gotthardt.util.ImprovedResourceTest;
import bo.gotthardt.util.InMemoryEbeanServer;
import com.avaje.ebean.EbeanServer;
import com.google.common.net.HttpHeaders;
import com.sun.jersey.api.client.ClientResponse;
import com.yammer.dropwizard.auth.oauth.OAuthProvider;
import org.joda.time.Duration;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static bo.gotthardt.util.fest.DropwizardAssertions.assertThat;

/**
 * Tests for the OAuth2 functionality working end-to-end.
 *
 * @author Bo Gotthardt
 */
public class OAuth2IntegrationTest extends ImprovedResourceTest {
    private final EbeanServer ebean = new InMemoryEbeanServer();

    @Override
    protected void setUpResources() throws Exception {
        addResource(new OAuth2AccessTokenResource(ebean));
        addResource(new UserResource(ebean));

        addProvider(OAuth2AuthorizationRequestProvider.class);
        addProvider(new OAuthProvider<User>(new OAuth2Authenticator(ebean), "realm"));
    }

    @Test
    public void shouldCreateAndSendTokenThatIdentifiesUser() {
        User user = new User("test", "blah");
        ebean.save(user);

        ClientResponse response = POST("/token/?grant_type=password&username=test&password=blah", null);
        assertThat(response).hasStatus(Response.Status.OK);

        OAuth2AccessToken token = response.getEntity(OAuth2AccessToken.class);
        // The token sent in the response won't have any user information, but if we get it from the database it will have.
        assertThat(ebean.find(OAuth2AccessToken.class, token.getAccessToken()).getUser().getId())
                .isEqualTo(user.getId());
    }

    @Test
    public void shouldNotCreateTokenForInvalidCredentials() {
        User user = new User("test", "blah");
        ebean.save(user);

        assertThat(POST("/token/?grant_type=password&username=test&password=WRONGPASSWORD", null))
                .hasStatus(Response.Status.UNAUTHORIZED);

        assertThat(ebean.find(OAuth2AccessToken.class).findRowCount())
                .isEqualTo(0);
    }

    @Test
    public void shouldNotCreateTokenForNonexistentCredentials() {
        assertThat(POST("/token/?grant_type=password&username=test&password=blah", null))
                .hasStatus(Response.Status.UNAUTHORIZED);

        assertThat(ebean.find(OAuth2AccessToken.class).findRowCount())
                .isEqualTo(0);
    }

    @Test
    public void should400WhenMissingGrantType() {
        assertThat(POST("/token", null))
                .hasStatus(Response.Status.BAD_REQUEST);
        assertThat(POST("/token/?grant_type=password&username=test", null))
                .hasStatus(Response.Status.BAD_REQUEST);

        assertThat(POST("/token/?grant_type=password&password=blah", null))
                .hasStatus(Response.Status.BAD_REQUEST);

    }

    @Test
    public void should400WhenGrantTypePasswordMissingUsername() {
        assertThat(POST("/token/?grant_type=password&password=blah", null))
                .hasStatus(Response.Status.BAD_REQUEST);

    }

    @Test
    public void should400WhenGrantTypePasswordMissingPassword() {
        assertThat(POST("/token/?grant_type=password&username=test", null))
                .hasStatus(Response.Status.BAD_REQUEST);

    }

    @Test
    public void shouldRefuseUnauthorizedAccessToAuthProtectedResource() {
        assertThat(GET("/users/1"))
                .hasStatus(Response.Status.UNAUTHORIZED);
    }

    @Test
    public void shouldAllowAuthorizedAccessToProtectedResource() {
        User user = new User("test", "blah");
        ebean.save(user);

        OAuth2AccessToken token = POST("/token/?grant_type=password&username=test&password=blah", null).getEntity(OAuth2AccessToken.class);

        ClientResponse response = client().resource("/users/" + user.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .get(ClientResponse.class);

        assertThat(response)
                .hasStatus(Response.Status.OK)
                .hasJsonContent(user);
    }

    @Test
    public void shouldInvalidateTokens() {
        User user = new User("test", "blah");
        ebean.save(user);

        OAuth2AccessToken token = new OAuth2AccessToken(user, Duration.standardHours(1));
        ebean.save(token);

        ClientResponse response = client().resource("/token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .delete(ClientResponse.class);
        ebean.refresh(token);

        assertThat(token.isValid()).isFalse();
    }
}
