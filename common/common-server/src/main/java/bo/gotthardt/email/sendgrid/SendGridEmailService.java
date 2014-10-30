package bo.gotthardt.email.sendgrid;

import bo.gotthardt.email.EmailService;
import bo.gotthardt.email.EmailServiceConfiguration;
import com.codahale.metrics.MetricRegistry;
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
    private final MetricRegistry metrics;

    @Inject
    public SendGridEmailService(HasSendGridConfiguration config, MetricRegistry metrics) {
        this.config = config.getEmail();
        this.metrics = metrics;
        this.sendGrid = new SendGrid(config.getSendGrid().getUsername(), config.getSendGrid().getPassword());
    }

    @Override
    public void send(String toAddress, String subject, String htmlContent) {
        SendGrid.Email email = new SendGrid.Email();
        email.setSubject(subject);
        email.setHtml(htmlContent);

        email.setFrom(config.getFromEmail());
        email.setFromName(config.getFromName());

        if (!Strings.isNullOrEmpty(config.getOverrideReceiver())) {
            email.addTo(config.getOverrideReceiver());
        } else {
            email.addTo(toAddress);
        }

        try {
            SendGrid.Response response = sendGrid.send(email);

            if (!response.getStatus()) {
                metrics.meter(MetricRegistry.name("email", "send", "failure", toMetricName(toAddress))).mark();
                log.error("Error sending email via SendGrid:", response.getMessage());
            } else {
                metrics.meter(MetricRegistry.name("email", "send", "success", toMetricName(toAddress))).mark();
            }
        } catch (SendGridException e) {
            metrics.meter(MetricRegistry.name("email", "send", "failure", toMetricName(toAddress))).mark();
            log.error("Error sending email via SendGrid:", e);
        }
    }

    private static String toMetricName(String email) {
        return email.replace(".", "-"); // TODO This probably needs to be more comprehensive.
    }
}
