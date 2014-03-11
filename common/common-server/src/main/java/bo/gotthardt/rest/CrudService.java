package bo.gotthardt.rest;

import bo.gotthardt.Persistable;
import bo.gotthardt.exception.NotFoundException;
import bo.gotthardt.jersey.provider.ListFiltering;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Bo Gotthardt
 */
@RequiredArgsConstructor
public class CrudService<T extends Persistable> {
    private final Class<T> type;
    private final EbeanServer ebean;

    public T fetchById(long id) {
        T item = ebean.find(type, id);

        if (item == null) {
            throw new NotFoundException(id);
        }

        return item;
    }

    public List<T> fetchByFilter(ListFiltering filter) {
        Query<T> dbQuery = ebean.find(type);

        filter.applyToQuery(dbQuery);

        return dbQuery.findList();
    }

    public T create(T item) {
        ebean.save(item);
        return item;
    }

    public T update(long id, T item) {
        assertExists(id);

        item.setId(id);
        ebean.update(item);
        return item;
    }

    public void delete(long id) {
        assertExists(id);

        ebean.delete(type, id);
    }

    /**
     * Assert that an object with the specified ID exists.
     *
     * @param id the object ID
     */
    protected void assertExists(long id) {
        // Presumably this is (slightly) faster than retrieving the object.
        if (ebean.find(type).where().eq("id", id).findRowCount() != 1) {
            throw new NotFoundException(id);
        }
    }
}
