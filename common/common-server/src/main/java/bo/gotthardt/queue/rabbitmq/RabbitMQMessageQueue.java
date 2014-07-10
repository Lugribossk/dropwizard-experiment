package bo.gotthardt.queue.rabbitmq;

import bo.gotthardt.queue.MessageQueue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import io.dropwizard.jackson.Jackson;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Bo Gotthardt
 */
@Slf4j
class RabbitMQMessageQueue<T> implements MessageQueue<T> {
    private static ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final Channel channel;
    private final String name;

    RabbitMQMessageQueue(Channel channel, String name) {
        this.channel = channel;
        this.name = name;
        try {
            channel.queueDeclare(name, true, false, false, null);
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publish(T message) {
        try {
            channel.basicPublish("", name, MessageProperties.PERSISTENT_TEXT_PLAIN, MAPPER.writeValueAsBytes(message));
            if (log.isTraceEnabled()) {
                log.trace("Published to '{}' with data '{}'.", name, MAPPER.writeValueAsString(message));
            }
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public QueueingConsumer consume() {
        QueueingConsumer consumer = new QueueingConsumer(channel);
        try {
            channel.basicConsume(name, false, consumer);
            return consumer;
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void acknowledge(QueueingConsumer.Delivery delivery) {
        try {
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reject(QueueingConsumer.Delivery delivery) {
        try {
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }
}
