package bo.gotthardt.email.sendgrid;

import bo.gotthardt.email.EmailService;
import bo.gotthardt.email.EmailServiceConfiguration;
import com.google.common.base.Strings;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

/**
 * Email service that uses SendGrid.
 *
 * @author Bo Gotthardt
 */
@Slf4j
public class SendGridEmailService implements EmailService {
    private final EmailServiceConfiguration config;
    private final SendGrid sendGrid;

    @Inject
    public SendGridEmailService(SendGridConfiguration sendGridConfig, EmailServiceConfiguration emailconfig) {
        config = emailconfig;
        sendGrid = new SendGrid(sendGridConfig.getUsername(), sendGridConfig.getPassword());
    }

    @Override
    public void send(String toAddress, String subject, String htmlContent) {
        SendGrid.Email email = new SendGrid.Email();
        email.setSubject(subject);
        email.setHtml(htmlContent);

        email.setFrom(config.getFromEmail());
        email.setFromName(config.getFromEmail());

        if (!Strings.isNullOrEmpty(config.getOverrideReceiver())) {
            email.addTo(config.getOverrideReceiver());
        } else {
            email.addTo(toAddress);
        }

        try {
            SendGrid.Response response = sendGrid.send(email);
            if (!response.getStatus()) {
                log.error("Error sending email via SendGrid:", response.getMessage());
            }
        } catch (SendGridException e) {
            log.error("Error sending email via SendGrid:", e);
        }
    }
}
