package bo.gotthardt.user;

import bo.gotthardt.model.User;
import bo.gotthardt.test.ApiIntegrationTest;
import bo.gotthardt.test.DummyAuthProvider;
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
    private static final DummyAuthProvider authProvider = new DummyAuthProvider();
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource(db))
            .addResource(authProvider)
            .build();

    private User user;

    @Override
    public ResourceTestRule getResources() {
        return resources;
    }

    @Before
    public void setupUser() {
        user = new User("testname", "testpassword");
        db.save(user);
        authProvider.setUser(user);
    }

    @Test
    public void shouldGetById() {
        assertThat(GET("/users/" + user.getId()))
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
