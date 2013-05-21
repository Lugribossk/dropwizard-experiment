package bo.gotthardt.api;

import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.model.User;
import bo.gotthardt.model.todo.TodoItem;
import bo.gotthardt.model.todo.TodoList;
import bo.gotthardt.util.DummyAuthProvider;
import bo.gotthardt.util.ImprovedResourceTest;
import bo.gotthardt.util.InMemoryEbeanServer;
import com.avaje.ebean.EbeanServer;
import org.junit.Test;

import static bo.gotthardt.util.fest.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class TodoListResourceTest extends ImprovedResourceTest {
    private final DummyAuthProvider authProvider = new DummyAuthProvider();
    private final EbeanServer ebean = new InMemoryEbeanServer();

    @Override
    protected void setUpResources() throws Exception {
        addResource(new TodoListResource(ebean));
        addProvider(authProvider);
        addProvider(ListFilteringProvider.class);
    }

    @Test
    public void blah() {
        User user = new User("test", "blah");
        ebean.save(user);
        authProvider.setUser(user);

        TodoList list = new TodoList("testlist", user);
        list.getItems().add(new TodoItem("testitem1"));
        list.getItems().add(new TodoItem("testitem2"));
        ebean.save(list);

        assertThat(GET("/todolists/" + list.getId()))
                .hasJsonContent(list);
    }
}
