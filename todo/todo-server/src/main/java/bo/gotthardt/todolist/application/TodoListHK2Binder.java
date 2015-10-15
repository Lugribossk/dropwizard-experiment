package bo.gotthardt.todolist.application;

import bo.gotthardt.ebean.EbeanBundle;
import bo.gotthardt.model.Widget;
import bo.gotthardt.queue.rabbitmq.RabbitMQBundle;
import bo.gotthardt.rest.CrudService;
import com.avaje.ebean.EbeanServer;
import com.codahale.metrics.MetricRegistry;
import io.dropwizard.setup.Environment;
import lombok.RequiredArgsConstructor;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@RequiredArgsConstructor
public class TodoListHK2Binder extends AbstractBinder {
    private final Environment environment;
    private final TodoListConfiguration configuration;
    private final EbeanBundle ebeanBundle;
    private final RabbitMQBundle rabbitMqBundle;

    @Override
    protected void configure() {
        bind(environment.metrics()).to(MetricRegistry.class);
        bind(ebeanBundle.getEbeanServer()).to(EbeanServer.class);

//        bindFactory(rabbitMqBundle.getQueueFactory("username", User.class))
//            .to(new TypeLiteral<MessageQueue<User>>() {})
//            .named("username");

        bindFactory(WidgetCrudFactory.class).to(new TypeLiteral<CrudService<Widget>>(){});

        // guice
//        bind(EmailService.class).toProvider(EmailServiceProvider.class);
//        bind(new TypeLiteral<HasSendGridConfiguration>(){}).toInstance(configuration);
    }
}
