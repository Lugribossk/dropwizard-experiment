package bo.gotthardt.api;

import bo.gotthardt.jersey.provider.ListFiltering;
import bo.gotthardt.Persistable;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.yammer.metrics.annotation.Timed;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Bo Gotthardt
 */
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class RestEndpoint<P extends Persistable> {
    private final Class<P> type;
    private final EbeanServer ebean;

    @GET
    @Path("/{id}")
    @Timed
    public P one(@PathParam("id") long id) {
        P item = ebean.find(type, id);

        if (item == null) {
            throw new NotFoundJsonException(id);
        }

        return item;
    }

    @GET
    public List<P> many(@Context ListFiltering filtering) {
        Query<P> dbQuery = ebean.find(type);

        filtering.applyToQuery(dbQuery);

        return dbQuery.findList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public P create(@Valid P item) {
        ebean.save(item);

        // TODO validation
        // TODO disallow updating sensitive properties

        return item;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public P update(@Valid P item, @PathParam("id") long id) {
        assertExists(id);

        // TODO validation
        // TODO disallow updating sensitive properties

        item.setId(id);
        ebean.update(item);

        return item;
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        assertExists(id);

        ebean.delete(type, id);
    }

    /**
     * Assert that an object with the specified ID exists.
     *
     * @param id the object ID
     */
    private void assertExists(long id) {
        // Presumably this is (slightly) faster than retrieving the object.
        if (ebean.find(type).where().eq("id", id).findRowCount() != 1) {
            throw new NotFoundJsonException(id);
        }
    }
}
