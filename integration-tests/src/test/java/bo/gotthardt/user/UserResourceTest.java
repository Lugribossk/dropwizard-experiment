package bo.gotthardt.user;

import bo.gotthardt.model.User;
import bo.gotthardt.test.ApiIntegrationTest;
import bo.gotthardt.test.DummyAuthFactory;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * Tests for {@link bo.gotthardt.user.UserResource}.
 *
 * @author Bo Gotthardt
 */
public class UserResourceTest extends ApiIntegrationTest {
    private static final DummyAuthFactory authFactory = new DummyAuthFactory();
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource(db))
            .addResource(AuthFactory.binder(authFactory))
            .setMapper(Jackson.newObjectMapper().registerModule(new JSR310Module()))
            .build();

    private User user;

    @Override
    public ResourceTestRule getResources() {
        return resources;
    }

    @Before
    public void setupUser() {
        user = new User("testname", "testpassword", "Testuser");
        db.save(user);
        authFactory.setUser(user);
    }

    @Test
    public void shouldGetById() {
        assertThat(GET("/users/" + user.getId().toString()))
                .hasJsonContent(user);
    }

    @Test
    public void shouldGetCurrent() {
        assertThat(GET("/users/current"))
                .hasJsonContent(user);
    }

    @Test
    public void shouldChangePassword() {
        POST("/users/current/password", formParameters("currentPassword", "testpassword", "newPassword", "testpassword2"));

        db.refresh(user);
        assertThat(user.getPassword().equalsPlaintext("testpassword2")).isTrue();
    }

    @Test
    public void shouldNotChangePasswordWhenCurrentPasswordIsWrong() {
        POST("/users/current/password", formParameters("currentPassword", "WRONGPASSWORD", "newPassword", "testpassword2"));

        db.refresh(user);
        assertThat(user.getPassword().equalsPlaintext("testpassword")).isTrue();
    }
}
