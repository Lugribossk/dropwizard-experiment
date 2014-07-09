package bo.gotthardt.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import io.dropwizard.jackson.Jackson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Bo Gotthardt
 */
@Slf4j
@RequiredArgsConstructor
abstract public class QueueWorker<T> implements Runnable {
    private static ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final MessageQueue<T> queue;
    private final Class<T> type;

    abstract boolean process(T message) throws Exception;

    public void run() {
        Channel channel = queue.getChannel();
        QueueingConsumer consumer = new QueueingConsumer(channel);
        try {
            channel.basicConsume(queue.getName(), false, consumer);
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }

        while (true) {
            QueueingConsumer.Delivery delivery;
            try {
                delivery = consumer.nextDelivery();
            } catch (InterruptedException e) {
                // TODO
                throw new RuntimeException(e);
            }
            long deliveryTag = delivery.getEnvelope().getDeliveryTag();

            boolean processed;
            try {
                T message = MAPPER.readValue(delivery.getBody(), type);

                log.trace("Received message '{}' with data '{}'.", deliveryTag, new String(delivery.getBody()));
                processed = process(message);
            } catch (Exception e) {
                log.warn("Processing message failed with exception:", e);
                processed = false;
            }

            try {
                if (processed) {
                    log.info("Processed message '{}' succesfully.", deliveryTag);
                    channel.basicAck(deliveryTag, false);
                } else {
                    log.info("Processing message '{}' failed.", deliveryTag);
                    channel.basicNack(deliveryTag, false, true);
                }
            } catch (IOException e) {
                // TODO
                throw new RuntimeException(e);
            }
        }
    }
}
