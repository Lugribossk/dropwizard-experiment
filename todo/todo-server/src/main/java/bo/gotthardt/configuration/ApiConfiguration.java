package bo.gotthardt.configuration;

import io.dropwizard.Configuration;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Bo Gotthardt
 */
@Getter
public class ApiConfiguration extends Configuration {
    @NotEmpty
    private String example;
}
