package bo.gotthardt.queue;

import lombok.Getter;

/**
 * @author Bo Gotthardt
 */
@Getter
public class WorkerConfiguration {
    private Class<? extends QueueWorker> worker;
    private int threads = 1;
}
