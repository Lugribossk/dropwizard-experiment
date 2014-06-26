package bo.gotthardt.user;

import bo.gotthardt.model.EmailVerification;
import bo.gotthardt.model.HashedValue;
import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * @author Bo Gotthardt
 */
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {
    private static final Duration TOKEN_LIFETIME = Duration.standardDays(2);
    private final EbeanServer db;

    public void requestPasswordReset(String username) {
        User user = db.find(User.class).where().disjunction()
                .eq("username", username)
                .eq("email", username).findUnique();

        if (user != null) {
            EmailVerification verify = db.find(EmailVerification.class).where()
                    .eq("user", user)
                    .eq("type", EmailVerification.Type.PASSWORD_RESET)
                    .lt("expirationDate", DateTime.now())
                    .findUnique();

            if (verify != null) {
                verify.setExpirationDate(DateTime.now().plus(TOKEN_LIFETIME));
                db.save(verify);
            } else {
                verify = new EmailVerification(user, TOKEN_LIFETIME, EmailVerification.Type.PASSWORD_RESET);
                db.save(verify);
            }

            log.info("Emailing link to '{}' to {}", verify.getUrl(), user.getEmail());
        }
    }

    public void doPasswordReset(String token, String newPassword) {
        EmailVerification verify = db.find(EmailVerification.class, token);

        if (verify != null) {
            User user = verify.getUser();
            if (verify.isValid() && verify.getType() == EmailVerification.Type.PASSWORD_RESET) {
                user.setPassword(new HashedValue(newPassword));
                db.save(user);
                verify.setExpirationDate(DateTime.now());
                db.save(verify);

                log.info("Changed password for user {} from email token.", user);
            } else {
                log.info("Tried to change password for {} with invalid token", user);
            }
        } else {
            log.info("Tried to change password with non-existent token [}", token);
        }
    }
}
