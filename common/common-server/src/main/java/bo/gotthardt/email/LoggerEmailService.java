package bo.gotthardt.email;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

/**
 * Email service that logs to console instead of actually sending.
 *
 * @author Bo Gotthardt
 */
@Slf4j
public class LoggerEmailService implements EmailService {
    @Inject
    public LoggerEmailService() { }

    @Override
    public void send(String toAddress, String subject, String htmlContent) {
        log.info("Intended to send email to {} with subject '{}'.", toAddress, subject);
        log.trace("Intended to send email to {} with body {}", toAddress, htmlContent);
    }
}
