package bo.gotthardt;

import com.sun.jersey.api.core.HttpContext;

import javax.ws.rs.ext.Provider;

/**
 * @author Bo Gotthardt
 */
@Provider
public class ListFilteringProvider extends AbstractInjectableProvider<ListFiltering> {
    public ListFilteringProvider() {
        super(ListFiltering.class);
    }

    @Override
    public ListFiltering getValue(HttpContext c) {
        return ListFiltering.fromQueryParameters(c.getRequest().getQueryParameters());
    }
}
