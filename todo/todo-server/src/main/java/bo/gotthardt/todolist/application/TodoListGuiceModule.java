package bo.gotthardt.todolist.application;

import bo.gotthardt.ebean.EbeanBundle;
import bo.gotthardt.email.EmailService;
import bo.gotthardt.email.EmailServiceProvider;
import bo.gotthardt.email.sendgrid.HasSendGridConfiguration;
import bo.gotthardt.model.User;
import bo.gotthardt.model.Widget;
import bo.gotthardt.queue.MessageQueue;
import bo.gotthardt.queue.rabbitmq.RabbitMQBundle;
import bo.gotthardt.rest.CrudService;
import com.avaje.ebean.EbeanServer;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import io.dropwizard.setup.Environment;
import lombok.RequiredArgsConstructor;

/**
 * Guice module for injecting dependencies in the TodoListApplication.
 *
 * @author Bo Gotthardt
 */
@RequiredArgsConstructor
public class TodoListGuiceModule extends AbstractModule {
    private final Environment environment;
    private final TodoListConfiguration configuration;
    private final EbeanBundle ebeanBundle;
    private final RabbitMQBundle rabbitMqBundle;

    @Override
    protected void configure() {
        bind(MetricRegistry.class).toInstance(environment.metrics());
        bind(EbeanServer.class).toInstance(ebeanBundle.getEbeanServer());
        bind(EmailService.class).toProvider(EmailServiceProvider.class);
        bind(new TypeLiteral<HasSendGridConfiguration>(){}).toInstance(configuration);

        bind(new TypeLiteral<MessageQueue<User>>(){})
            .annotatedWith(Names.named("username"))
            .toProvider(() -> rabbitMqBundle.getQueue("username", User.class));
    }

    @Provides
    public CrudService<Widget> getWidgetService(EbeanServer db) {
        return new CrudService<>(Widget.class, db);
    }
}
