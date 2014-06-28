package bo.gotthardt.email;

import bo.gotthardt.email.sendgrid.HasSendGridConfiguration;
import bo.gotthardt.email.sendgrid.SendGridConfiguration;
import lombok.Getter;
import org.junit.Test;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

public class EmailsTest {
    @Test
    public void shouldCreateLoggerServiceWhenEmailsAreDisabled() {
        TestConfiguration config = new TestConfiguration();
        config.getEmail().setEnabled(false);

        EmailService email = Emails.createService(config);

        assertThat(email).isInstanceOf(LoggerEmailService.class);
    }

    @Getter
    private static class TestConfiguration implements HasEmailServiceConfiguration, HasSendGridConfiguration {
        private EmailServiceConfiguration email = new EmailServiceConfiguration();
        private SendGridConfiguration sendGrid;
    }
}