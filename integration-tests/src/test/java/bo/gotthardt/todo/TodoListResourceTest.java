package bo.gotthardt.todo;

import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.model.User;
import bo.gotthardt.model.todo.TodoItem;
import bo.gotthardt.model.todo.TodoList;
import bo.gotthardt.todolist.rest.TodoListResource;
import bo.gotthardt.test.ApiIntegrationTest;
import bo.gotthardt.test.DummyAuthProvider;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class TodoListResourceTest extends ApiIntegrationTest {
    private static final DummyAuthProvider authProvider = new DummyAuthProvider();
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TodoListResource(db))
            .addResource(authProvider)
            .addResource(new ListFilteringProvider())
            .build();

    @Test
    public void blah() {
        User user = new User("test", "blah");
        db.save(user);
        authProvider.setUser(user);

        TodoList list = new TodoList("testlist", user);
        list.getItems().add(new TodoItem("testitem1"));
        list.getItems().add(new TodoItem("testitem2"));
        db.save(list);

        assertThat(GET("/todolists/" + list.getId()))
                .hasJsonContent(list);
    }

    @Override
    public ResourceTestRule getResources() {
        return resources;
    }
}
