package bo.gotthardt.api;

import bo.gotthardt.api.exception.NotFoundException;
import bo.gotthardt.api.exception.UnauthorizedException;
import bo.gotthardt.model.User;
import com.google.code.morphia.Datastore;
import com.yammer.dropwizard.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

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
    private final Datastore ds;

    @GET
    @Path("/{id}")
    public User one(@Auth User user, @PathParam("id") ObjectId id) {
        User item = ds.get(User.class, id);

        if (item == null) {
            throw new NotFoundException(id);
        }

        if (!item.isAccessibleBy(user)) {
            throw new UnauthorizedException();
        }

        return item;
    }
}
