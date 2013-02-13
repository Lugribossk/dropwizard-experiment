package bo.gotthardt;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Bo Gotthardt
 */
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class RestEndpoint<T extends Persistable> {
    private final Class<T> type;

    @GET
    @Path("/{id}")
    @Timed
    public T one(@PathParam("id") long id) {
        T item = Ebean.find(type, id);

        if (item == null) {
            throw new NotFoundJsonException(id);
        }

        return item;
    }

    @GET
    public List<T> many(@QueryParam("q") Optional<String> query,
                        @QueryParam("limit") Optional<Integer> limit,
                        @QueryParam("offset") Optional<Integer> offset) {

        Query<T> dbQuery = Ebean.find(type).orderBy().asc("id");
        if (limit.isPresent()) {
            dbQuery = dbQuery.setMaxRows(limit.get()).setFirstRow(offset.or(50));
        }
        if (query.isPresent()) {
            dbQuery = dbQuery.where().like("name", "%" + query.get() + "%").query();
        }

        return dbQuery.findList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public T create(T item) {
        Ebean.save(item);

        // TODO validation
        // TODO disallow updating sensitive properties

        return item;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public T update(T item, @PathParam("id") long id) {
        assertExists(id);

        // TODO validation
        // TODO disallow updating sensitive properties

        item.setId(id);
        Ebean.update(item);

        return item;
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        assertExists(id);

        Ebean.delete(type, id);
    }

    /**
     * Assert that an object with the specified ID exists.
     *
     * @param id the object ID
     */
    private void assertExists(long id) {
        // Presumably this is (slightly) faster than retrieving the object.
        if (Ebean.find(type).where().eq("id", id).findRowCount() != 1) {
            throw new NotFoundJsonException(id);
        }
    }
}
