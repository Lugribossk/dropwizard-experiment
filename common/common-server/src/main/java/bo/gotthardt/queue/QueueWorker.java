package bo.gotthardt.queue;

import com.google.common.base.Preconditions;

import java.util.function.Function;

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
abstract public class QueueWorker<T> implements Runnable {
    protected final MessageQueue<T> queue;
    protected Function<Void, Void> cancel;

    public QueueWorker(MessageQueue<T> queue) {
        this.queue = queue;
    }

    protected abstract Void process(T message);

    @Override
    public void run() {
        cancel = queue.consume(this::process);
    }

    public void cancel() {
        Preconditions.checkNotNull(cancel, "Cannot cancel before run.");
        cancel.apply(null);
    }
}
