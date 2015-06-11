package bo.gotthardt.rest;

import bo.gotthardt.Persistable;
import bo.gotthardt.exception.NotFoundException;
import bo.gotthardt.jersey.parameters.ListFiltering;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 * @author Bo Gotthardt
 */
public class CrudService<T extends Persistable> {
    private final Class<T> type;
    private final EbeanServer db;

    @Inject
    public CrudService(Class<T> type, EbeanServer db) {
        this.type = type;
        this.db = db;
    }

    public T fetchById(UUID id) {
        T item = db.find(type, id);

        if (item == null) {
            throw new NotFoundException(id);
        }

        return item;
    }

    public List<T> fetchByFilter(ListFiltering filter) {
        Query<T> dbQuery = db.find(type);

        filter.applyToQuery(dbQuery);

        return dbQuery.findList();
    }

    public T create(T item) {
        db.save(item);
        return item;
    }

    public T update(UUID id, T item) {
        assertExists(id);

        item.setId(id);
        db.update(item);
        return item;
    }

    public void delete(UUID id) {
        assertExists(id);

        db.delete(type, id);
    }

    /**
     * Assert that an object with the specified ID exists.
     *
     * @param id the object ID
     */
    protected void assertExists(UUID id) {
        // Presumably this is (slightly) faster than retrieving the object.
        if (db.find(type).where().eq("id", id).findRowCount() != 1) {
            throw new NotFoundException(id);
        }
    }
}
