package bo.gotthardt.todolist.rest;

import bo.gotthardt.model.Widget;
import bo.gotthardt.rest.CrudService;
import bo.gotthardt.rest.RestResource;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("/widgets")
public class WidgetResource extends RestResource<Widget> {
    @Inject
    public WidgetResource(CrudService<Widget> service) {
        super(service);
    }
}
