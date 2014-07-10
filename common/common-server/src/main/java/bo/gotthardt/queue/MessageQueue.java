package bo.gotthardt.queue;

import com.rabbitmq.client.QueueingConsumer;

/**
 * A named queue of messages of a particular type.
 *
 * @author Bo Gotthardt
 */
public interface MessageQueue<T> {
    void publish(T message);

    QueueingConsumer consume();

    void acknowledge(QueueingConsumer.Delivery delivery);

    void reject(QueueingConsumer.Delivery delivery);
}
