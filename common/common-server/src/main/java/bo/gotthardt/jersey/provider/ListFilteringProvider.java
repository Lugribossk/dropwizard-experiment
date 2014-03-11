package bo.gotthardt.jersey.provider;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 * Jersey injectable provider that enables resource methods to have {@link ListFiltering} parameters.
 *
 * @author Bo Gotthardt
 */
@Provider
public class ListFilteringProvider extends AbstractInjectableProvider<Context, ListFiltering> {
    private static final String SEARCH_QUERY = "q";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";
    private static final String ORDER_BY = "orderBy";
    private static final String ORDER_SORT = "orderSort";

    public ListFilteringProvider() {
        super(ListFiltering.class);
    }

    @Override
    public ListFiltering getValue(HttpContext c) {
        return fromQueryParameters(c.getRequest().getQueryParameters());
    }

    private static ListFiltering fromQueryParameters(MultivaluedMap<String, String> queryParameters) {
        ListFiltering filtering = new ListFiltering();

        if (queryParameters.containsKey(SEARCH_QUERY)) {
            filtering.setSearchQuery(Optional.of(queryParameters.getFirst(SEARCH_QUERY)));
        }

        if (queryParameters.containsKey(LIMIT)) {
            try {
                filtering.setLimit(Integer.parseInt(queryParameters.getFirst(LIMIT)));
            } catch (NumberFormatException e) {
                // Do nothing.
            }
        }

        if (queryParameters.containsKey(OFFSET)) {
            try {
                filtering.setOffset(Integer.parseInt(queryParameters.getFirst(OFFSET)));
            } catch (NumberFormatException e) {
                // Do nothing.
            }
        }

        if (queryParameters.containsKey(ORDER_BY)) {
            filtering.setOrderProperty(queryParameters.getFirst(ORDER_BY));
        }

        if (queryParameters.containsKey(ORDER_SORT)) {
            if ("desc".equals(queryParameters.getFirst(ORDER_SORT))) {
                filtering.setSortOrder(ListFiltering.SortOrder.DESCENDING);
            }
        }

        return filtering;
    }
}
