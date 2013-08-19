package bo.gotthardt.api;

import bo.gotthardt.model.todo.TodoList;
import com.google.code.morphia.Datastore;

import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("/todolists")
public class TodoListResource extends RestrictedRestResource<TodoList> {
    public TodoListResource(Datastore ds) {
        super(TodoList.class, ds);
    }
}
