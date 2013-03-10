package bo.gotthardt.jersey.provider;

import com.avaje.ebean.Query;
import com.google.common.base.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Options for how a list should be filtered and sorted.
 *
 * @author Bo Gotthardt
 */
@Getter
@Setter
@NoArgsConstructor
public class ListFiltering {
    private static final String SEARCH_QUERY = "q";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";
    private static final String ORDER_BY = "orderBy";
    private static final String ORDER_SORT = "orderSort";

    private Optional<String> searchQuery = Optional.absent();
    private int limit = 0;
    private int offset = 50;
    private String orderProperty = "id";
    private SortOrder sortOrder = SortOrder.ASCENDING;

    public Query<?> applyToQuery(Query<?> dbQuery) {
        if (sortOrder == SortOrder.ASCENDING) {
            dbQuery.order().asc(orderProperty);
        } else {
            dbQuery.order().desc(orderProperty);
        }

        if (limit != 0) {
            dbQuery.setMaxRows(limit).setFirstRow(offset);
        }

        if (searchQuery.isPresent()) {
            // TODO Make this not hard-coded
            dbQuery.where().like("name", "%" + searchQuery.get() + "%");
        }

        return dbQuery;
    }

    public static ListFiltering fromQueryParameters(MultivaluedMap<String, String> queryParameters) {
        ListFiltering filtering = new ListFiltering();

        if (queryParameters.containsKey(SEARCH_QUERY)) {
            filtering.searchQuery = Optional.of(queryParameters.getFirst(SEARCH_QUERY));
        }

        if (queryParameters.containsKey(LIMIT)) {
            try {
                filtering.limit = Integer.parseInt(queryParameters.getFirst(LIMIT));
            } catch (NumberFormatException e) {
                // Do nothing.
            }
        }

        if (queryParameters.containsKey(OFFSET)) {
            try {
                filtering.offset = Integer.parseInt(queryParameters.getFirst(OFFSET));
            } catch (NumberFormatException e) {
                // Do nothing.
            }
        }

        if (queryParameters.containsKey(ORDER_BY)) {
            filtering.orderProperty = queryParameters.getFirst(ORDER_BY);
        }

        if (queryParameters.containsKey(ORDER_SORT)) {
            if ("desc".equals(queryParameters.getFirst(ORDER_SORT))) {
                filtering.sortOrder = SortOrder.DESCENDING;
            }
        }

        return filtering;
    }

    public static enum SortOrder {
        ASCENDING,
        DESCENDING
    }
}
