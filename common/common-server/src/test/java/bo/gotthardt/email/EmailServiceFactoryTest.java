package bo.gotthardt.email;

import bo.gotthardt.email.sendgrid.HasSendGridConfiguration;
import bo.gotthardt.email.sendgrid.SendGridConfiguration;
import lombok.Getter;
import org.junit.Test;
import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;


public class EmailServiceFactoryTest {
    @Test
    public void shouldCreateLoggerServiceWhenEmailsAreDisabled() {
        TestConfiguration config = new TestConfiguration();
        config.getEmail().setEnabled(false);

        EmailService email = new EmailServiceFactory(config, null).provide();

        assertThat(email).isInstanceOf(LoggerEmailService.class);
    }

    @Getter
    private static class TestConfiguration implements HasSendGridConfiguration {
        private EmailServiceConfiguration email = new EmailServiceConfiguration();
        private SendGridConfiguration sendGrid;
    }
}