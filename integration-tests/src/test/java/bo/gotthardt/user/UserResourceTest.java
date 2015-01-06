package bo.gotthardt.user;

import bo.gotthardt.model.User;
import bo.gotthardt.test.ApiIntegrationTest;
import bo.gotthardt.test.DummyAuthFactory;
import bo.gotthardt.test.ResourceTestRule2;
import io.dropwizard.auth.AuthFactory;
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
    public static final ResourceTestRule2 resources = ResourceTestRule2.builder()
            .addResource(new UserResource(db))
            .addResource(AuthFactory.binder(authFactory))
            .build();

    private User user;

    @Override
    public ResourceTestRule2 getResources2() {
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
