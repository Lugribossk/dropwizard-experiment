package bo.gotthardt.queue;

import java.util.function.Function;

/**
 * A named queue of messages of a particular type.
 * Messages are published to the queue and then consumed asynchronously by a separate worker.
 *
 * @author Bo Gotthardt
 */
public interface MessageQueue<T> {
    /**
     * Publish a message.
     * @param message The message.
     */
    void publish(T message);

    /**
     * Get/wait for messages from the queue and run the specified processing function on them.
     * @param processor The processing function.
     */
    void consume(Function<T, Void> processor);
}
