package bo.gotthardt.user;

import bo.gotthardt.exception.NotFoundException;
import bo.gotthardt.exception.UnauthorizedException;
import bo.gotthardt.model.HashedValue;
import bo.gotthardt.model.TOTPSecret;
import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import io.dropwizard.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Bo Gotthardt
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class UserResource {
    private final EbeanServer db;

    @Inject
    public UserResource(EbeanServer db) {
        this.db = db;
    }

    @GET
    @Path("/{id}")
    public User one(@Auth User user, @PathParam("id") long id) {
        User item = db.find(User.class, id);

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

    @POST
    @Path("/current/password")
    public void changePasswordLoggedIn(@Auth User user, @FormParam("currentPassword") String currentPassword, @FormParam("newPassword") String newPassword) {
        if (!user.getPassword().equalsPlaintext(currentPassword)) {
            log.info("Tried to change password for user {}, but current one was incorrect.", user);
            throw new UnauthorizedException();
        }

        user.setPassword(new HashedValue(newPassword));
        db.save(user);

        log.info("Changed password for user {}.", user);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/current/totp")
    public String changeTotp(@Auth User user, @FormParam("token") Integer totpToken) {
        if (user.getTotpSecret() != null) {
            if (totpToken == null || !user.getTotpSecret().isCorrectTokenAt(totpToken, DateTime.now())) {
                log.info("Tried to change TOTP secret for user {}, but one already exists and current token was incorrect.", user);
                throw new UnauthorizedException();
            }
        }

        // TODO Somehow check that the user has added the secret to their client correctly before saving it.
        TOTPSecret secret = TOTPSecret.create();
        user.setTotpSecret(secret);
        db.save(user);

        log.info("Changed TOTP secret for user {}.", user);
        return secret.getEncoded("Todo", user.getUsername());
    }
}
