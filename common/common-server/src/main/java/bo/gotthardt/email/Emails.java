package bo.gotthardt.email;

import bo.gotthardt.email.sendgrid.HasSendGridConfiguration;
import bo.gotthardt.email.sendgrid.SendGridEmailService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Bo Gotthardt
 */
@Slf4j
public class Emails {
    public static <T extends HasEmailServiceConfiguration & HasSendGridConfiguration> EmailService createService(T config) {
        if (!config.getEmail().isEnabled() || config.getSendGrid() == null) {
            log.info("Email sending disabled, logging them to console instead.");
            return new LoggerEmailService();
        } else {
            log.info("Email sending enabled with SendGrid username {}.", config.getSendGrid().getUsername());
            return new SendGridEmailService(config.getSendGrid(), config.getEmail());
        }
    }
}
