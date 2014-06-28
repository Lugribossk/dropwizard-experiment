package bo.gotthardt.email.sendgrid;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Configuration for using SendGrid.
 *
 * @author Bo Gotthardt
 */
@Getter
@Setter
public class SendGridConfiguration {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
