package bo.gotthardt.queue;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Dropwizard command for running message queue workers.
 * Can be configured with which worker classes to instantiate, and how many threads of each.
 *
 * Due to weirdness with the Dropwizard initialization order, a Guice Injector must be set after creation.
 * Make sure it has been set up for providing all the dependencies required for the workers being used.
 *
 * @author Bo Gotthardt
 */
@Slf4j
public class QueueWorkersCommand<T extends Configuration & HasWorkerConfigurations> extends EnvironmentCommand<T> {
    @Setter
    private Injector injector;

    /**
     * Constructor.
     * @param application The application running this command.
     */
    public QueueWorkersCommand(Application<T> application) {
        super(application, "workers", "Runs message queue worker threads.");
    }

    @Override
    protected void run(Environment environment, Namespace namespace, T configuration) throws Exception {
        Preconditions.checkNotNull(injector, "Injector not set, perhaps the run() execution order changed?");

        List<WorkerConfiguration> workerConfigurations = configuration.getWorkers();

        workerConfigurations.forEach(config -> {
            Class<? extends QueueWorker> workerClass = config.getWorker();
            ExecutorService executorService = environment.lifecycle()
                    .executorService(workerClass.getSimpleName() + "-%d")
                    .maxThreads(config.getThreads())
                    .build();

            for (int i = 0; i < config.getThreads(); i++) {
                QueueWorker<?> worker = injector.getInstance(workerClass);
                executorService.submit(worker);
            }
            log.info("Created {} thread{} for worker {}.", config.getThreads(), config.getThreads() > 1 ? "s" : "", workerClass.getSimpleName());
        });
    }
}
