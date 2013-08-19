package bo.gotthardt.application;

import bo.gotthardt.api.WidgetResource;
import bo.gotthardt.configuration.ApiConfiguration;
import bo.gotthardt.jersey.filter.AllowAllOriginsFilter;
import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.morphia.MorphiaBundle;
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
    private MorphiaBundle morphia;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        Bootstrap<ApiConfiguration> bootstrap2 = (Bootstrap<ApiConfiguration>) bootstrap;
        morphia = new MorphiaBundle();

        bootstrap2.addBundle(morphia);
        bootstrap.addBundle(new OAuth2Bundle(morphia.getDatastore()));

        bootstrap.getObjectMapperFactory().enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        bootstrap.getObjectMapperFactory().enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        environment.addResource(new WidgetResource(morphia.getDatastore()));

        environment.addProvider(ListFilteringProvider.class);

        environment.addFilter(AllowAllOriginsFilter.class, configuration.getHttpConfiguration().getRootPath());

        environment.addHealthCheck(new VersionHealthCheck());
    }
}
