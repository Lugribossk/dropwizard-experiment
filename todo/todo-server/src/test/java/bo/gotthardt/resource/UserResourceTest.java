package bo.gotthardt.resource;

import bo.gotthardt.model.User;
import bo.gotthardt.util.ApiIntegrationTest;
import bo.gotthardt.util.DummyAuthProvider;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import static bo.gotthardt.util.assertj.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class UserResourceTest extends ApiIntegrationTest {
    private static final DummyAuthProvider authProvider = new DummyAuthProvider();
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource(ebean))
            .addResource(authProvider)
            .build();

    @Test
    public void blah() {
        User user = new User("test", "blah");
        ebean.save(user);
        authProvider.setUser(user);

        assertThat(GET("/users/" + user.getId()))
                .hasJsonContent(user);
    }

    @Override
    public ResourceTestRule getResources() {
        return resources;
    }
}
