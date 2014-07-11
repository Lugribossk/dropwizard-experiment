package bo.gotthardt.queue;

import java.util.List;

/**
 * Configuration for which {@link bo.gotthardt.queue.QueueWorker} subclasses that {@link bo.gotthardt.queue.QueueWorkersCommand} should start.
 *
 * @author Bo Gotthardt
 */
public interface HasWorkerConfigurations {
    List<WorkerConfiguration> getWorkers();
}
