package bo.gotthardt.application;

import bo.gotthardt.configuration.ApiConfiguration;
import bo.gotthardt.ebean.EbeanBundle;
import bo.gotthardt.jersey.filter.AllowAllOriginsFilter;
import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.model.Widget;
import bo.gotthardt.oauth2.OAuth2Bundle;
import bo.gotthardt.rest.resource.WidgetResource;
import bo.gotthardt.rest.CrudService;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new WidgetResource(new CrudService<>(Widget.class, ebeanBundle.getDefaultServer())));

        environment.jersey().register(new ListFilteringProvider());

        environment.servlets().addFilter("cors", new AllowAllOriginsFilter());

        environment.healthChecks().register("version", new VersionHealthCheck());
    }
}
