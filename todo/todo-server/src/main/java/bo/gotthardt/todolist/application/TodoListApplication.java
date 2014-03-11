package bo.gotthardt.todolist.application;

import bo.gotthardt.application.VersionHealthCheck;
import bo.gotthardt.ebean.EbeanBundle;
import bo.gotthardt.jersey.filter.AllowAllOriginsFilter;
import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.model.Widget;
import bo.gotthardt.oauth2.OAuth2Bundle;
import bo.gotthardt.rest.CrudService;
import bo.gotthardt.todolist.rest.WidgetResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author Bo Gotthardt
 */
public class TodoListApplication extends Application<TodoListConfiguration> {
    private EbeanBundle ebeanBundle;

    public static void main(String... args) throws Exception {
        new TodoListApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<TodoListConfiguration> bootstrap) {
        ebeanBundle = new EbeanBundle();
        bootstrap.addBundle(ebeanBundle);
        bootstrap.addBundle(new OAuth2Bundle(ebeanBundle.getEbeanServer()));
    }

    @Override
    public void run(TodoListConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new WidgetResource(new CrudService<>(Widget.class, ebeanBundle.getEbeanServer())));

        environment.jersey().register(new ListFilteringProvider());

        environment.servlets().addFilter("cors", new AllowAllOriginsFilter());

        environment.healthChecks().register("version", new VersionHealthCheck());
    }
}