package bo.gotthardt.email;

import bo.gotthardt.email.sendgrid.HasSendGridConfiguration;
import bo.gotthardt.email.sendgrid.SendGridEmailService;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;

@Slf4j
public class EmailServiceFactory implements Factory<EmailService> {
    private final HasSendGridConfiguration config;
    private final ServiceLocator locator;

    @Inject
    public EmailServiceFactory(HasSendGridConfiguration config, ServiceLocator locator) {
        this.config = config;
        this.locator = locator;
    }

    @Override
    public EmailService provide() {
        if (!config.getEmail().isEnabled() || config.getSendGrid() == null) {
            log.info("Email sending disabled, logging them to console instead.");
            return new LoggerEmailService();
        } else {
            log.info("Email sending enabled with SendGrid username {}.", config.getSendGrid().getUsername());
            return locator.getService(SendGridEmailService.class);
        }
    }

    @Override
    public void dispose(EmailService instance) {
        // Empty on purpose.
    }
}
