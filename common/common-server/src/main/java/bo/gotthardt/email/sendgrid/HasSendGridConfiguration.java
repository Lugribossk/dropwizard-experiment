package bo.gotthardt.email.sendgrid;

import bo.gotthardt.email.HasEmailServiceConfiguration;

/**
 * @author Bo Gotthardt
 */
public interface HasSendGridConfiguration extends HasEmailServiceConfiguration {
    SendGridConfiguration getSendGrid();
}
