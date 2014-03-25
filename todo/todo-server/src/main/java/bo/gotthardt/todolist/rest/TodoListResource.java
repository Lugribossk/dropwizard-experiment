package bo.gotthardt.todolist.rest;

import bo.gotthardt.model.todo.TodoList;
import bo.gotthardt.rest.CrudService;
import bo.gotthardt.rest.RestrictedRestResource;
import com.avaje.ebean.EbeanServer;

import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("/todolists")
public class TodoListResource extends RestrictedRestResource<TodoList> {
    public TodoListResource(EbeanServer db) {
        super(new CrudService<>(TodoList.class, db));
    }
}
