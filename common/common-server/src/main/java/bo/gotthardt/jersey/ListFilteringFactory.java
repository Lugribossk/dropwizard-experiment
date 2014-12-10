package bo.gotthardt.jersey;

import bo.gotthardt.jersey.provider.ListFiltering;
import com.google.common.base.Optional;
import io.dropwizard.jersey.params.IntParam;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.ws.rs.QueryParam;

/**
 * @author Bo Gotthardt
 */
public class ListFilteringFactory implements Factory<ListFiltering> {
    @QueryParam("q")
    private String searchQuery;

    @QueryParam("limit")
    private IntParam limit;

    @QueryParam("offset")
    private IntParam offset;

    @QueryParam("orderBy")
    private String orderBy;

    @QueryParam("orderSort")
    private String orderSort;

    @Override
    public ListFiltering provide() {
        ListFiltering filtering = new ListFiltering();

        if (searchQuery != null) {
            filtering.setSearchQuery(Optional.of(searchQuery));
        }

        if (limit != null) {
            filtering.setLimit(limit.get());
        }

        if (offset != null) {
            filtering.setOffset(offset.get());
        }

        if (orderBy != null) {
            filtering.setOrderProperty(orderBy);
        }

        if (orderSort != null) {
            if ("desc".equals(orderSort)) {
                filtering.setSortOrder(ListFiltering.SortOrder.DESCENDING);
            }
        }

        return filtering;
    }

    @Override
    public void dispose(ListFiltering instance) {
        // Empty on purpose.
    }

    public static Binder getBinder() {
        return new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(ListFilteringFactory.class).to(ListFiltering.class);
            }
        };
    }
}
