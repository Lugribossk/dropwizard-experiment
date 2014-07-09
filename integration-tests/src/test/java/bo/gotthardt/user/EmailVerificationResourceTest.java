package bo.gotthardt.user;

import bo.gotthardt.email.LoggerEmailService;
import bo.gotthardt.model.EmailVerification;
import bo.gotthardt.model.User;
import bo.gotthardt.test.ApiIntegrationTest;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;
import static org.assertj.jodatime.api.Assertions.assertThat;

/**
 * Tests for {@link bo.gotthardt.user.EmailVerificationResource}.
 *
 * @author Bo Gotthardt
 */
public class EmailVerificationResourceTest extends ApiIntegrationTest {
    private static final PasswordResetService service = new PasswordResetService(db, new LoggerEmailService());
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EmailVerificationResource(db, service))
            .build();

    private User user;

    @Override
    public ResourceTestRule getResources() {
        return resources;
    }

    @Before
    public void setupUser() {
        user = new User("testname", "testpassword");
        db.save(user);
    }

    @Test
    public void shouldGetByToken() {
        EmailVerification verify = new EmailVerification(user, Duration.standardDays(2), EmailVerification.Type.PASSWORD_RESET);
        db.save(verify);

        assertThat(GET("/verifications/" + verify.getToken()))
                .hasJsonContent(verify);
    }

    @Test
    public void shouldCreateVerificationTokenForUsername() {
        POST("/verifications/passwordreset", formParameters("username", "testname"));

        List<EmailVerification> verifys = db.find(EmailVerification.class).findList();
        assertThat(verifys).hasSize(1);
        assertThat(verifys.get(0).getUser()).isEqualTo(user);
        assertThat(verifys.get(0).getType()).isEqualTo(EmailVerification.Type.PASSWORD_RESET);
        assertThat(verifys.get(0).getExpirationDate()).isAfter(DateTime.now());
    }

    @Test
    public void shouldCreateVerificationTokenForEmail() {
        user.setEmail("example@example.com");
        db.save(user);

        POST("/verifications/passwordreset", formParameters("username", "example@example.com"));

        List<EmailVerification> verifys = db.find(EmailVerification.class).findList();
        assertThat(verifys).hasSize(1);
        assertThat(verifys.get(0).getUser()).isEqualTo(user);
        assertThat(verifys.get(0).getType()).isEqualTo(EmailVerification.Type.PASSWORD_RESET);
        assertThat(verifys.get(0).getExpirationDate()).isAfter(DateTime.now());
    }

    @Test
    public void shouldChangePasswordWhenUsed() {
        EmailVerification verify = new EmailVerification(user, Duration.standardDays(2), EmailVerification.Type.PASSWORD_RESET);
        db.save(verify);

        POST("/verifications/" + verify.getToken() + "/passwordreset", formParameters("newPassword", "testpassword2"));

        db.refresh(user);
        assertThat(user.getPassword().equalsPlaintext("testpassword2")).isTrue();
    }
}
