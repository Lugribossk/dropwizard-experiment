package bo.gotthardt.queue;

import lombok.Getter;

/**
 * Configuration for a {@link bo.gotthardt.queue.QueueWorker} subclass that {@link WorkersCommand} should start.
 *
 * @author Bo Gotthardt
 */
@Getter
public class WorkerConfiguration {
    /** The fully qualified class name of the worker. */
    private Class<? extends QueueWorker> worker;
    /** How many threads to start with this worker. */
    private int threads = 1;
}
