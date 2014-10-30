package bo.gotthardt.email;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Configuration for sending emails.
 *
 * @author Bo Gotthardt
 */
@Getter
@Setter
public class EmailServiceConfiguration {
    /**
     * Whether email sending is enabled.
     * If disabled, emails will be logged to the console instead of being sent.
     */
    private boolean enabled = false;
    /**
     * The sender's email address in outgoing emails.
     */
    @NotEmpty
    private String fromEmail;
    /**
     * The sender's name in outgoing emails.
     */
    @NotEmpty
    private String fromName;
    /**
     * Always send all emails to this address, instead of their intended recipient.
     */
    private String overrideReceiver;
}
