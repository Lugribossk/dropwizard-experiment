package bo.gotthardt.resource;

import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.model.User;
import bo.gotthardt.model.todo.TodoItem;
import bo.gotthardt.model.todo.TodoList;
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
public class TodoListResourceTest {
    private static final DummyAuthProvider authProvider = new DummyAuthProvider();
    private static final EbeanServer ebean = new InMemoryEbeanServer();
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TodoListResource(ebean))
            .addResource(authProvider)
            .addResource(new ListFilteringProvider())
            .build();
    public final RestHelper rest = new RestHelper(resources);

    @Test
    public void blah() {
        User user = new User("test", "blah");
        ebean.save(user);
        authProvider.setUser(user);

        TodoList list = new TodoList("testlist", user);
        list.getItems().add(new TodoItem("testitem1"));
        list.getItems().add(new TodoItem("testitem2"));
        ebean.save(list);

        assertThat(rest.GET("/todolists/" + list.getId()))
                .hasJsonContent(list);
    }
}
