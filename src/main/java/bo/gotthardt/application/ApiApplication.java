package bo.gotthardt.application;

import bo.gotthardt.api.PersonEndpoint;
import bo.gotthardt.configuration.ApiConfiguration;
import bo.gotthardt.ebean.EbeanHealthCheck;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * @author Bo Gotthardt
 */
public class ApiApplication extends Service<ApiConfiguration> {
    public static void main(String... args) throws Exception {
        new ApiApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApiConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));

        bootstrap.getObjectMapperFactory().enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        bootstrap.getObjectMapperFactory().enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        environment.addResource(new PersonEndpoint());

        environment.addHealthCheck(new EbeanHealthCheck());
    }
}
