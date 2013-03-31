package bo.gotthardt.jersey.provider;

import com.sun.jersey.api.core.HttpContext;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 * Jersey injectable provider that enables resource methods to have {@link ListFiltering} parameters.
 *
 * @author Bo Gotthardt
 */
@Provider
public class ListFilteringProvider extends AbstractInjectableProvider<Context, ListFiltering> {
    public ListFilteringProvider() {
        super(ListFiltering.class);
    }

    @Override
    public ListFiltering getValue(HttpContext c) {
        return ListFiltering.fromQueryParameters(c.getRequest().getQueryParameters());
    }
}
