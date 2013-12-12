package bo.gotthardt.resource;

import bo.gotthardt.model.User;
import bo.gotthardt.util.DummyAuthProvider;
import bo.gotthardt.util.InMemoryEbeanServer;
import bo.gotthardt.util.RestHelper;
import com.avaje.ebean.EbeanServer;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import static bo.gotthardt.util.assertj.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class UserResourceTest {
    private static final DummyAuthProvider authProvider = new DummyAuthProvider();
    private static final EbeanServer ebean = new InMemoryEbeanServer();
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource(ebean))
            .addResource(authProvider)
            .build();
    public final RestHelper rest = new RestHelper(resources);

    @Test
    public void blah() {
        User user = new User("test", "blah");
        ebean.save(user);
        authProvider.setUser(user);

        assertThat(rest.GET("/users/" + user.getId()))
                .hasJsonContent(user);
    }
}
