package bo.gotthardt.rest.resource;

import bo.gotthardt.model.TodoList;
import bo.gotthardt.rest.CrudService;
import bo.gotthardt.rest.RestrictedRestResource;
import com.avaje.ebean.EbeanServer;

import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("/todolists")
public class TodoListResource extends RestrictedRestResource<TodoList> {
    public TodoListResource(EbeanServer ebean) {
        super(new CrudService<>(TodoList.class, ebean));
    }
}
