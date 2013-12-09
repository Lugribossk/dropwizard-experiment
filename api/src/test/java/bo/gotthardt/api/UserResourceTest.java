package bo.gotthardt.api;

import bo.gotthardt.model.User;
import bo.gotthardt.util.DummyAuthProvider;
import bo.gotthardt.util.ImprovedResourceTest;
import bo.gotthardt.util.InMemoryEbeanServer;
import com.avaje.ebean.EbeanServer;
import org.junit.Test;

import static bo.gotthardt.util.assertj.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class UserResourceTest extends ImprovedResourceTest {
    private final DummyAuthProvider authProvider = new DummyAuthProvider();
    private final EbeanServer ebean = new InMemoryEbeanServer();

    @Override
    protected void setUpResources() throws Exception {
        addResource(new UserResource(ebean));
        addProvider(authProvider);
    }

    @Test
    public void blah() {
        User user = new User("test", "blah");
        ebean.save(user);
        authProvider.setUser(user);

        assertThat(GET("/users/" + user.getId()))
                .hasJsonContent(user);
    }
}
