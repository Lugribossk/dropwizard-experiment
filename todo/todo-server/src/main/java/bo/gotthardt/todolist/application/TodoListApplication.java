package bo.gotthardt.todolist.application;

import bo.gotthardt.application.VersionHealthCheck;
import bo.gotthardt.ebean.EbeanBundle;
import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.model.User;
import bo.gotthardt.model.Widget;
import bo.gotthardt.oauth2.OAuth2Bundle;
import bo.gotthardt.rest.CrudService;
import bo.gotthardt.todo.TodoClientBundle;
import bo.gotthardt.todolist.rest.UserResource;
import bo.gotthardt.todolist.rest.WidgetResource;
import com.avaje.ebean.EbeanServer;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * @author Bo Gotthardt
 */
public class TodoListApplication extends Application<TodoListConfiguration> {
    @Getter
    private EbeanBundle ebeanBundle;

    public static void main(String... args) throws Exception {
        new TodoListApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<TodoListConfiguration> bootstrap) {
        ebeanBundle = new EbeanBundle();
        bootstrap.addBundle(ebeanBundle);
        bootstrap.addBundle(new OAuth2Bundle(ebeanBundle));
        bootstrap.addBundle(new TodoClientBundle());
    }

    @Override
    public void run(TodoListConfiguration configuration, Environment environment) throws Exception {
        EbeanServer db = ebeanBundle.getEbeanServer();

        environment.jersey().register(new WidgetResource(new CrudService<>(Widget.class, db)));
        environment.jersey().register(new UserResource(db));

        environment.jersey().register(new ListFilteringProvider());

        environment.jersey().setUrlPattern("/api");

        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter("allowedOrigins", "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowedMethods", "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
        filter.setInitParameter("allowCredentials", "true");

        environment.healthChecks().register("version", new VersionHealthCheck());

        User user = new User("test", "test");
        user.setName("Test Testsen");
        user.setEmail("example@example.com");
        db.save(user);
    }
}