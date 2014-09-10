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
     * Asynchronously continually wait for messages from the queue and run the specified processing function on them when they arrive.
     * If the processing function throws an exception, the current message will not be removed from the queue.
     * @param processor The processing function.
     * @return A function that cancels the consumer when run.
     */
    Function<Void, Void> consume(Function<T, Void> processor);

    /**
     * Get the next message in the queue, blocking until there is one.
     * <b>You probably want to use {@link #consume(Function)} instead.</b>
     * @return The message.
     */
    T consumeNext();

    /**
     * Get the name of this queue.
     * @return The name.
     */
    String getName();
}
