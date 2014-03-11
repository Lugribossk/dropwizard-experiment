package bo.gotthardt.configuration;

import bo.gotthardt.ebean.EbeanConfiguration;
import bo.gotthardt.ebean.HasEbeanConfiguration;
import io.dropwizard.Configuration;
import lombok.Getter;

/**
 * @author Bo Gotthardt
 */
@Getter
public class ApiConfiguration extends Configuration implements HasEbeanConfiguration {
    private EbeanConfiguration ebean;
}
