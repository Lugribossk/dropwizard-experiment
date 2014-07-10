package bo.gotthardt.queue;

import bo.gotthardt.queue.rabbitmq.HasRabbitMQConfiguration;
import bo.gotthardt.queue.rabbitmq.RabbitMQBundle;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author Bo Gotthardt
 */
@Slf4j
public class QueueWorkersCommand<T extends Configuration & HasRabbitMQConfiguration & HasWorkerConfigurations> extends EnvironmentCommand<T> {
    private final RabbitMQBundle rabbitMq;
    private final Map<String, Class<?>> queues;

    public QueueWorkersCommand(Application<T> application, RabbitMQBundle rabbitMq, Map<String, Class<?>> queues) {
        super(application, "workers", "Runs message queue worker threads.");
        this.rabbitMq = rabbitMq;
        this.queues = queues;
    }

    @Override
    protected void run(Environment environment, Namespace namespace, T configuration) throws Exception {
        Injector injector = createInjector();

        List<WorkerConfiguration> workerConfigurations = configuration.getWorkers();

        workerConfigurations.forEach(config -> {
            Class<? extends QueueWorker> workerClass = config.getWorker();
            ExecutorService executorService = environment.lifecycle().executorService(workerClass.getSimpleName() + "-%d").maxThreads(config.getThreads()).build();

            for (int i = 0; i < config.getThreads(); i++) {
                QueueWorker<?> worker = injector.getInstance(workerClass);
                executorService.submit(worker);
            }
            log.info("Created {} threads for worker [}.", config.getThreads(), workerClass.getSimpleName());
        });
    }

    private Injector createInjector() {
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                queues.forEach((name, klass) -> {
                    queueBind(klass, name);
                });
            }

            private <S> void queueBind(Class<S> type, String name) {
                bind(new TypeLiteral<MessageQueue<S>>(){})
                        .annotatedWith(Names.named(name))
                        .toProvider(() -> rabbitMq.getQueue(name));
            }
        });
    }
}
