package bo.gotthardt.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.rabbitmq.client.QueueingConsumer;
import io.dropwizard.jackson.Jackson;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * A worker runs a processing function on messages from a specific message queue.
 *
 * @author Bo Gotthardt
 */
@Slf4j
abstract public class QueueWorker<T> implements Runnable {
    private static ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final Class<T> type;
    private final MessageQueue<T> queue;

    public QueueWorker(Class<T> type, MessageQueue<T> queue) {
        this.type = type;
        this.queue = queue;
    }

    protected abstract void process(T message);

    @Override
    public void run() {
        QueueingConsumer consumer = queue.consume();

        //noinspection InfiniteLoopStatement
        while (true) {
            QueueingConsumer.Delivery delivery;
            try {
                delivery = consumer.nextDelivery();
            } catch (InterruptedException e) {
                // TODO
                throw new RuntimeException(e);
            }
            long deliveryTag = delivery.getEnvelope().getDeliveryTag();

            try {
                T message = MAPPER.readValue(delivery.getBody(), type);

                log.trace("Received message '{}' with data '{}'.", deliveryTag, new String(delivery.getBody()));
                Stopwatch timer = Stopwatch.createStarted();

                process(message);

                queue.acknowledge(delivery);
                log.info("Processed {} message '{}' succesfully in {} ms.", type.getSimpleName(), deliveryTag, timer.stop().elapsed(TimeUnit.MILLISECONDS));
            } catch (Exception e) {
                log.warn("Processing message failed with exception:", e);
                queue.reject(delivery);
            }
        }
    }
}
