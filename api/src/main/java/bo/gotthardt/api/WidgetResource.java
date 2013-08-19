package bo.gotthardt.api;

import bo.gotthardt.model.Widget;
import com.google.code.morphia.Datastore;

import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("/widgets")
public class WidgetResource extends RestResource<Widget> {
    public WidgetResource(Datastore ds) {
        super(Widget.class, ds);
    }
}
