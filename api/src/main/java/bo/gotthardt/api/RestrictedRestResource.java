package bo.gotthardt.api;

import bo.gotthardt.AccessibleBy;
import bo.gotthardt.Persistable;
import bo.gotthardt.api.exception.UnauthorizedException;
import bo.gotthardt.jersey.provider.ListFiltering;
import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.yammer.dropwizard.auth.Auth;

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
public class RestrictedRestResource<P extends Persistable & AccessibleBy<User>> extends RestResource<P> {

    public RestrictedRestResource(Class<P> type, EbeanServer ebean) {
        super(type, ebean);
    }

    @GET
    @Path("/{id}")
    public P one(@Auth User user, @PathParam("id") long id) {
        P item = super.one(id);

        if (!item.isAccessibleBy(user)) {
            throw new UnauthorizedException();
        }

        return item;
    }

    @GET
    public List<P> many(@Auth final User user, @Context ListFiltering filtering) {
        List<P> items = super.many(filtering);

        return Lists.newArrayList(Collections2.filter(items, new Predicate<P>() {
            @Override
            public boolean apply(P input) {
                return input.isAccessibleBy(user);
            }
        }));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public P create(@Auth User user, @Valid P item) {
        return super.create(item);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public P update(@Auth User user, @Valid P item, @PathParam("id") long id) {
        if (!item.isAccessibleBy(user)) {
            throw new UnauthorizedException();
        }

        return super.update(item, id);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@Auth User user, @PathParam("id") long id) {
        P item = super.one(id);

        if (!item.isAccessibleBy(user)) {
            throw new UnauthorizedException();
        }

        super.delete(id);
    }
}
