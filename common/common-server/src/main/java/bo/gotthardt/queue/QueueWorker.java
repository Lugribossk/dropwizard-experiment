package bo.gotthardt.queue;

import lombok.extern.slf4j.Slf4j;

/**
 * A worker that runs a processing function on messages from a specific message queue.
 * Subclass this to create workers for specific tasks.
 *
 * Having this as a concrete class rather than passing the processing function directly to the
 * queue lets us make the link between queue and function explicit. It also allows the worker to be instantiated by
 * Guice without TypeLiteral binding configuration craziness.
 *
 * @author Bo Gotthardt
 */
@Slf4j
abstract public class QueueWorker<T> implements Runnable {
    private final Class<T> type;
    private final MessageQueue<T> queue;

    public QueueWorker(Class<T> type, MessageQueue<T> queue) {
        this.type = type;
        this.queue = queue;
    }

    protected abstract Void process(T message);

    @Override
    public void run() {
        queue.consume(this::process, type);
    }
}
