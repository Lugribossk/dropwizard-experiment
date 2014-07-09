package bo.gotthardt.email;

import bo.gotthardt.email.sendgrid.HasSendGridConfiguration;
import bo.gotthardt.email.sendgrid.SendGridEmailService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Guice {@link Provider} for {@link EmailService}s.
 *
 * @author Bo Gotthardt
 */
@Slf4j
public class EmailServiceProvider implements Provider<EmailService> {
    private HasSendGridConfiguration config;

    @Inject
    public EmailServiceProvider(HasSendGridConfiguration config) {
        this.config = config;
    }

    @Override
    public EmailService get() {
        if (!config.getEmail().isEnabled() || config.getSendGrid() == null) {
            log.info("Email sending disabled, logging them to console instead.");
            return new LoggerEmailService();
        } else {
            log.info("Email sending enabled with SendGrid username {}.", config.getSendGrid().getUsername());
            return new SendGridEmailService(config.getSendGrid(), config.getEmail());
        }
    }
}
