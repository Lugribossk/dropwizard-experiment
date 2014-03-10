package bo.gotthardt.resource;

import bo.gotthardt.model.Widget;
import bo.gotthardt.resource.base.RestResource;
import bo.gotthardt.service.CrudService;

import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("/widgets")
public class WidgetResource extends RestResource<Widget> {
    public WidgetResource(CrudService<Widget> service) {
        super(service);
    }
}
