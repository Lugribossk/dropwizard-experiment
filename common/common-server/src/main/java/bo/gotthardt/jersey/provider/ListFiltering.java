package bo.gotthardt.jersey.provider;

import com.avaje.ebean.Query;
import com.google.common.base.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Options for how a list should be filtered and sorted.
 *
 * @author Bo Gotthardt
 */
@Getter
@Setter
@NoArgsConstructor
public class ListFiltering {
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

    public static enum SortOrder {
        ASCENDING,
        DESCENDING
    }
}
