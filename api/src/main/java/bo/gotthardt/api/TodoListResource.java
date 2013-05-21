package bo.gotthardt.api;

import bo.gotthardt.model.todo.TodoList;
import com.avaje.ebean.EbeanServer;

import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("/todolists")
public class TodoListResource extends RestrictedRestResource<TodoList> {
    public TodoListResource(EbeanServer ebean) {
        super(TodoList.class, ebean);
    }
}
