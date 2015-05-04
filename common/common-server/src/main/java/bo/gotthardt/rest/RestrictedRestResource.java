package bo.gotthardt.rest;

import bo.gotthardt.access.Owned;
import bo.gotthardt.jersey.parameters.ListFiltering;
import bo.gotthardt.model.User;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import io.dropwizard.auth.Auth;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Bo Gotthardt
 */
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class RestrictedRestResource<P extends Persistable & Owned> {
    private final CrudService<P> service;

    @GET
    @Path("/{id}")
    public P one(@Auth User user, @PathParam("id") long id) {
        P item = service.fetchById(id);

        item.assertOwnedBy(user);

        return item;
    }

    @GET
    public List<P> many(@Auth final User user, @Context ListFiltering filtering) {
        List<P> items = service.fetchByFilter(filtering);

        // TODO This is really really bad
        return Lists.newArrayList(Collections2.filter(items, input -> input.isOwnedBy(user)));
    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public P create(@Auth User user, @Valid P item) {
//        // TODO
//        return service.create(item);
//    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public P update(@Auth User user, @Valid P item, @PathParam("id") long id) {
        item.assertOwnedBy(user);

        return service.update(id, item);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@Auth User user, @PathParam("id") long id) {
        P item = service.fetchById(id);

        item.assertOwnedBy(user);

        service.delete(id);
    }
}
