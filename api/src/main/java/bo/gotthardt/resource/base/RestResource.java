package bo.gotthardt.resource.base;

import bo.gotthardt.Persistable;
import bo.gotthardt.jersey.provider.ListFiltering;
import bo.gotthardt.service.CrudService;
import com.codahale.metrics.annotation.Timed;
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
public class RestResource<P extends Persistable> {
    private final CrudService<P> service;

    @GET
    @Path("/{id}")
    @Timed
    public P one(@PathParam("id") long id) {
        return service.fetchById(id);
    }

    @GET
    public List<P> many(@Context ListFiltering filtering) {
        return service.fetchByFilter(filtering);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public P create(@Valid P item) {
        return service.create(item);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public P update(@PathParam("id") long id, @Valid P item) {
        return service.update(id, item);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        service.delete(id);
    }
}
