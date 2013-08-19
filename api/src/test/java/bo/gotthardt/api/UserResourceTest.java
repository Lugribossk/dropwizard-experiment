package bo.gotthardt.api;

import bo.gotthardt.model.User;
import bo.gotthardt.util.DummyAuthProvider;
import bo.gotthardt.util.ImprovedResourceTest;
import bo.gotthardt.util.InMemoryDatastore;
import com.google.code.morphia.Datastore;
import org.junit.Test;

import static bo.gotthardt.util.fest.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class UserResourceTest extends ImprovedResourceTest {
    private final DummyAuthProvider authProvider = new DummyAuthProvider();
    private final Datastore ds = new InMemoryDatastore();

    @Override
    protected void setUpResources() throws Exception {
        addResource(new UserResource(ds));
        addProvider(authProvider);
    }

    @Test
    public void blah() {
        User user = new User("test", "blah");
        ds.save(user);
        authProvider.setUser(user);

        assertThat(GET("/users/" + user.getId()))
                .hasJsonContent(user);
    }
}
