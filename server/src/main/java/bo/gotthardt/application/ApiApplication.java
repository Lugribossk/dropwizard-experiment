package bo.gotthardt.application;

import bo.gotthardt.jersey.filter.AllowAllOriginsFilter;
import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.api.PersonEndpoint;
import bo.gotthardt.configuration.ApiConfiguration;
import bo.gotthardt.ebean.EbeanBundle;
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
    private EbeanBundle ebeanBundle;

    public static void main(String... args) throws Exception {
        new ApiApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApiConfiguration> bootstrap) {
        ebeanBundle = new EbeanBundle();
        bootstrap.addBundle(ebeanBundle);
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));

        bootstrap.getObjectMapperFactory().enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        bootstrap.getObjectMapperFactory().enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        environment.addResource(new PersonEndpoint(ebeanBundle.getDefaultServer()));

        environment.addProvider(ListFilteringProvider.class);

        environment.addFilter(AllowAllOriginsFilter.class, "/api/*");

        environment.addHealthCheck(new VersionHealthCheck());
    }
}
