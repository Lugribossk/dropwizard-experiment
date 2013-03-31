package bo.gotthardt.application;

import bo.gotthardt.api.PersonEndpoint;
import bo.gotthardt.configuration.ApiConfiguration;
import bo.gotthardt.ebean.EbeanBundle;
import bo.gotthardt.jersey.filter.AllowAllOriginsFilter;
import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.oauth2.OAuth2Bundle;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * @author Bo Gotthardt
 */
public class ApiBundle implements ConfiguredBundle<ApiConfiguration> {
    private EbeanBundle ebeanBundle;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        ebeanBundle = new EbeanBundle();
        bootstrap.addBundle(ebeanBundle);
        bootstrap.addBundle(new OAuth2Bundle(ebeanBundle.getDefaultServer()));

        bootstrap.getObjectMapperFactory().enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        bootstrap.getObjectMapperFactory().enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        environment.addResource(new PersonEndpoint(ebeanBundle.getDefaultServer()));

        environment.addProvider(ListFilteringProvider.class);

        environment.addFilter(AllowAllOriginsFilter.class, configuration.getHttpConfiguration().getRootPath());

        environment.addHealthCheck(new VersionHealthCheck());
    }
}
