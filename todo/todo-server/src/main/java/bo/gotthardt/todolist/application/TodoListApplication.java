package bo.gotthardt.todolist.application;

import bo.gotthardt.application.VersionHealthCheck;
import bo.gotthardt.ebean.EbeanBundle;
import bo.gotthardt.email.EmailService;
import bo.gotthardt.email.EmailServiceProvider;
import bo.gotthardt.email.sendgrid.HasSendGridConfiguration;
import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.model.User;
import bo.gotthardt.model.Widget;
import bo.gotthardt.oauth2.OAuth2Bundle;
import bo.gotthardt.queue.MessageQueue;
import bo.gotthardt.queue.QueueWorkersCommand;
import bo.gotthardt.queue.rabbitmq.RabbitMQBundle;
import bo.gotthardt.rest.CrudService;
import bo.gotthardt.todo.TodoClientBundle;
import bo.gotthardt.todolist.rest.WidgetResource;
import bo.gotthardt.user.EmailVerificationResource;
import bo.gotthardt.user.UserResource;
import com.avaje.ebean.EbeanServer;
import com.google.common.base.Stopwatch;
import com.google.inject.*;
import com.google.inject.name.Names;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

/**
 * @author Bo Gotthardt
 */
@Slf4j
public class TodoListApplication extends Application<TodoListConfiguration> {
    @Getter
    private EbeanBundle ebeanBundle;
    private RabbitMQBundle rabbitMqBundle;
    private QueueWorkersCommand<TodoListConfiguration> workersCommand;

    public static void main(String... args) throws Exception {
        Stopwatch startupTimer = Stopwatch.createStarted();
        new TodoListApplication().run(args);
        log.info("Startup took {} ms.", startupTimer.stop().elapsed(TimeUnit.MILLISECONDS));
    }

    @Override
    public void initialize(Bootstrap<TodoListConfiguration> bootstrap) {
        ebeanBundle = new EbeanBundle();
        rabbitMqBundle = new RabbitMQBundle();

        bootstrap.addBundle(ebeanBundle);
        bootstrap.addBundle(rabbitMqBundle);
        bootstrap.addBundle(new OAuth2Bundle(ebeanBundle));
        bootstrap.addBundle(new TodoClientBundle());

        // The anonymous subclass seems to be needed for the config type to be picked up correctly.
        workersCommand = new QueueWorkersCommand<TodoListConfiguration>(this, rabbitMqBundle) {};
        bootstrap.addCommand(workersCommand);
    }

    @Override
    public void run(TodoListConfiguration configuration, Environment environment) throws Exception {
        Injector injector = createInjector(configuration);
        workersCommand.setInjector(injector);

        environment.jersey().register(injector.getInstance(WidgetResource.class));
        environment.jersey().register(injector.getInstance(UserResource.class));
        environment.jersey().register(injector.getInstance(EmailVerificationResource.class));

        environment.jersey().register(new ListFilteringProvider());

        environment.jersey().setUrlPattern("/api/*");

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
        ebeanBundle.getEbeanServer().save(user);

        rabbitMqBundle.getQueue("username").publish(user);
    }

    private Injector createInjector(TodoListConfiguration configuration) {
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(new TypeLiteral<HasSendGridConfiguration>(){}).toInstance(configuration);
                bind(EbeanServer.class).toProvider(ebeanBundle).asEagerSingleton();
                bind(EmailService.class).toProvider(EmailServiceProvider.class);

                bind(new TypeLiteral<MessageQueue<User>>(){})
                        .annotatedWith(Names.named("username"))
                        .toProvider(() -> rabbitMqBundle.getQueue("username"));
            }

            @Provides
            public CrudService<Widget> getWidgetService(EbeanServer db) {
                return new CrudService<>(Widget.class, db);
            }
        });
    }
}