package bo.gotthardt.api;

import bo.gotthardt.model.Widget;
import com.avaje.ebean.EbeanServer;

import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("/widgets")
public class WidgetResource extends RestResource<Widget> {
    public WidgetResource(EbeanServer ebean) {
        super(Widget.class, ebean);
    }
}
