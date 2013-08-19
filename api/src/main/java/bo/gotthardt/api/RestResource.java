package bo.gotthardt.api;

import bo.gotthardt.Persistable;
import bo.gotthardt.api.exception.NotFoundException;
import bo.gotthardt.jersey.provider.ListFiltering;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.yammer.metrics.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

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
public class RestResource<P extends Persistable> {
    private final Class<P> type;
    private final Datastore ds;

    @GET
    @Path("/{id}")
    @Timed
    public P one(@PathParam("id") ObjectId id) {
        P item = ds.get(type, id);

        if (item == null) {
            throw new NotFoundException(id);
        }

        return item;
    }

    @GET
    public List<P> many(@Context ListFiltering filtering) {
        Query<P> dbQuery = ds.find(type);

        filtering.applyToQuery(dbQuery);

        return dbQuery.asList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public P create(@Valid P item) {
        ds.save(item);

        // TODO validation
        // TODO disallow updating sensitive properties

        return item;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public P update(@Valid P item/*, @PathParam("id") String id*/) {
        assertExists(item.getId());

        // TODO validation
        // TODO disallow updating sensitive properties

        ds.save(item);

        return item;
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") ObjectId id) {
        assertExists(id);

        ds.delete(type, id);
    }

    /**
     * Assert that an object with the specified ID exists.
     *
     * @param id the object ID
     */
    protected void assertExists(ObjectId id) {
        if (ds.get(type, id) == null) {
            throw new NotFoundException(id);
        }
    }
}
