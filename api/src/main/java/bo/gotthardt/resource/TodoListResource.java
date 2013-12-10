package bo.gotthardt.resource;

import bo.gotthardt.model.todo.TodoList;
import bo.gotthardt.resource.base.RestrictedRestResource;
import bo.gotthardt.service.CrudService;
import com.avaje.ebean.EbeanServer;

import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("/todolists")
public class TodoListResource extends RestrictedRestResource<TodoList> {
    public TodoListResource(EbeanServer ebean) {
        super(new CrudService<TodoList>(TodoList.class, ebean));
    }
}
