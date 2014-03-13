package bo.gotthardt.todolist.rest;

import bo.gotthardt.exception.NotFoundException;
import bo.gotthardt.exception.UnauthorizedException;
import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import io.dropwizard.auth.Auth;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Bo Gotthardt
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class UserResource {
    private final EbeanServer ebean;

    @GET
    @Path("/{id}")
    public User one(@Auth User user, @PathParam("id") long id) {
        User item = ebean.find(User.class, id);

        if (item == null) {
            throw new NotFoundException(id);
        }

        if (!item.isAccessibleBy(user)) {
            throw new UnauthorizedException();
        }

        return item;
    }

    @GET
    @Path("/current")
    public User current(@Auth User user) {
        return user;
    }
}
