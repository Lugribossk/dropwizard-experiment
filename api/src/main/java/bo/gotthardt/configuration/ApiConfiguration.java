package bo.gotthardt.configuration;

import bo.gotthardt.morphia.HasMongoConfiguration;
import bo.gotthardt.morphia.MongoConfiguration;
import com.yammer.dropwizard.config.Configuration;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Bo Gotthardt
 */
@Getter
public class ApiConfiguration extends Configuration implements HasMongoConfiguration {
    @NotEmpty
    private String example;

    private MongoConfiguration mongodb;
}
