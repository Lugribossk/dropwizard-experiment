package bo.gotthardt.oauth2;

import bo.gotthardt.model.Customer;
import bo.gotthardt.model.OAuth2AccessToken;
import bo.gotthardt.model.User;
import bo.gotthardt.oauth2.authentication.OAuth2Authenticator;
import bo.gotthardt.oauth2.authorization.OAuth2AccessTokenResource;
import bo.gotthardt.oauth2.authorization.OAuth2AuthorizationRequestFactory;
import bo.gotthardt.test.InMemoryEbeanServer;
import bo.gotthardt.user.UserResource;
import com.codahale.metrics.MetricRegistry;
import com.google.common.net.HttpHeaders;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.oauth.OAuthFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.jackson.JacksonMessageBodyProvider;
import org.assertj.core.api.Assertions;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.glassfish.jersey.servlet.ServletProperties;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * Tests for the OAuth2 functionality working end-to-end.
 *
 * Since this uses OAuthFactory which uses @Context injection, we can't use ResourceTestRule since that uses
 * InMemoryTestContainer, which doesn't support context injection.
 *
 * @author Bo Gotthardt
 */
public class OAuth2IntegrationTest extends JerseyTest {
    protected static final InMemoryEbeanServer db = new InMemoryEbeanServer();

    private User user;

    @Before
    public void setup() {
        db.clear();
        Customer customer = new Customer("Test Customer");
        db.save(customer);
        user = new User("testuser", "testpass", "Testuser");
        user.setCustomer(customer);
        db.save(user);
    }

    @Test
    public void shouldCreateAndSendTokenThatIdentifiesUser() {
        Response response = POST("/token", loginParameters("testuser", "testpass"));
        assertThat(response).hasStatus(Response.Status.OK);

        OAuth2AccessToken token = response.readEntity(OAuth2AccessToken.class);
        // The token sent in the response won't have any user information, but if we get it from the database it will have.
        Assertions.assertThat(db.find(OAuth2AccessToken.class, token.getAccessToken()).getUser().getId())
            .isEqualTo(user.getId());
    }

    @Test
    public void shouldNotCreateTokenForInvalidCredentials() {
        assertThat(POST("/token", loginParameters("testuser", "WRONGPASSWORD")))
            .hasStatus(Response.Status.UNAUTHORIZED);

        assertThat(db.find(OAuth2AccessToken.class).findRowCount())
            .isEqualTo(0);
    }

    @Test
    public void shouldNotCreateTokenForNonexistentCredentials() {
        assertThat(POST("/token", loginParameters("DOESNOTEXIST", "testpass")))
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
        assertThat(POST("/token", loginParameters(null, "testpass")))
            .hasStatus(Response.Status.BAD_REQUEST);
    }

    @Test
    public void should400WhenGrantTypePasswordIsMissingPassword() {
        assertThat(POST("/token", loginParameters("testuser", null)))
            .hasStatus(Response.Status.BAD_REQUEST);

    }

    @Test
    public void shouldRefuseNonAuthorizedAccessToAuthProtectedResource() {
        assertThat(target("/users/" + user.getId()).request().get())
            .hasStatus(Response.Status.UNAUTHORIZED);
    }

    @Test
    public void shouldRefuseUnauthorizedAccessToAuthProtectedResource() {
        Response response = target("/users/" + user.getId()).request()
            .header(HttpHeaders.AUTHORIZATION, "Bearer WRONGTOKEN")
            .get();

        assertThat(response)
            .hasStatus(Response.Status.UNAUTHORIZED);
    }

    @Test
    public void shouldAllowAuthorizedAccessToProtectedResource() {
        OAuth2AccessToken token = POST("/token", loginParameters("testuser", "testpass"))
            .readEntity(OAuth2AccessToken.class);

        Invocation.Builder header = target("/users/" + user.getId()).request()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
        Response response = header.get();

        assertThat(response)
            .hasStatus(Response.Status.OK)
            .hasJsonContent(user);
    }

    @Test
    public void shouldInvalidateTokens() {
        OAuth2AccessToken token = new OAuth2AccessToken(user, Duration.standardHours(1));
        db.save(token);

        target("/token").request()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
            .delete();
        db.refresh(token);

        assertThat(token.isValid()).isFalse();
    }


    @Override
    protected TestContainerFactory getTestContainerFactory()
        throws TestContainerException {
        // GrizzlyWebTestContainerFactory works, GrizzlyTestContainerFactory returns error 500 in some of the tests...
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    protected DeploymentContext configureDeployment() {
        return ServletDeploymentContext.builder(new OAuth2IntegrationTestResourceConfig())
            .initParam(ServletProperties.JAXRS_APPLICATION_CLASS, OAuth2IntegrationTestResourceConfig.class.getName())
            .build();
    }

    public static class OAuth2IntegrationTestResourceConfig extends DropwizardResourceConfig {
        public OAuth2IntegrationTestResourceConfig() {
            super(true, new MetricRegistry());

            register(new JacksonMessageBodyProvider(Jackson.newObjectMapper(), Validation.buildDefaultValidatorFactory().getValidator()));
            register(new OAuth2AccessTokenResource(db));
            register(new UserResource(db));
            register(OAuth2AuthorizationRequestFactory.getBinder());
            register(AuthFactory.binder(new OAuthFactory<>(new OAuth2Authenticator(db), "OAuth2", User.class)));
        }
    }

    private Response POST(String path, MultivaluedMap input) {
        return target(path).request().post(Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
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
}
