package bo.gotthardt.rest;

import bo.gotthardt.Persistable;
import bo.gotthardt.jersey.parameters.ListFiltering;
import com.codahale.metrics.annotation.Timed;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

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
    public P one(@PathParam("id") UUID id) {
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
    public P update(@PathParam("id") UUID id, @Valid P item) {
        return service.update(id, item);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") UUID id) {
        service.delete(id);
    }
}
