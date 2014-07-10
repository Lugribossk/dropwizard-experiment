package bo.gotthardt.queue;

import java.util.List;

/**
 * @author Bo Gotthardt
 */
public interface HasWorkerConfigurations {
    List<WorkerConfiguration> getWorkers();
}
