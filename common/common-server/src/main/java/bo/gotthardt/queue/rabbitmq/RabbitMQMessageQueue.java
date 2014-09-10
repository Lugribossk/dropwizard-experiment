package bo.gotthardt.queue.rabbitmq;

import bo.gotthardt.queue.MessageQueue;
import bo.gotthardt.queue.MessageQueueException;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;
import io.dropwizard.jackson.Jackson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Function;

/**
 * RabbitMQ implementation of {@link bo.gotthardt.queue.MessageQueue}.
 *
 * To instantiate, see {@link bo.gotthardt.queue.rabbitmq.RabbitMQBundle#getQueue(String, Class)}.
 *
 * @author Bo Gotthardt
 */
@Slf4j
class RabbitMQMessageQueue<T> implements MessageQueue<T> {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final Channel channel;
    @Getter
    private final String name;
    private final Class<T> type;
    private final MetricRegistry metrics;
    private Meter publish;

    RabbitMQMessageQueue(Channel channel, String name, Class<T> type, MetricRegistry metrics) {
        this.channel = channel;
        this.name = name;
        this.type = type;
        this.metrics = metrics;
        this.publish = metrics.meter(MetricRegistry.name("queue", type.getSimpleName(), name, "publish"));
        try {
            channel.queueDeclare(name, true, false, false, null);
        } catch (IOException e) {
            throw new MessageQueueException("Unable to declare queue.", e);
        }
    }

    @Override
    public void publish(T message) {
        try {
            channel.basicPublish("", name, MessageProperties.PERSISTENT_TEXT_PLAIN, MAPPER.writeValueAsBytes(message));

            publish.mark();
            if (log.isTraceEnabled()) {
                log.trace("Published to '{}' with data '{}'.", name, MAPPER.writeValueAsString(message));
            }
        } catch (IOException e) {
            throw new MessageQueueException("Unable to publish to queue.", e);
        }
    }

    @Override
    public Function<Void, Void> consume(Function<T, Void> processor) {
        Consumer consumer = new FunctionConsumer<>(channel, processor, type, name, metrics);

        try {
            String tag = channel.basicConsume(name, false, consumer);
            log.info("Set up consumer '{}' for queue '{}'.", tag, name);

            return (x) -> {
                try {
                    channel.basicCancel(tag);
                } catch (IOException e) {
                    throw new MessageQueueException("Unable to cancel.", e);
                }
                return null;
            };
        } catch (IOException e) {
            throw new MessageQueueException("Unable to set up consumer.", e);
        }
    }

    @Override
    public T consumeNext() {
        try {
            GetResponse response = channel.basicGet(name, true);
            return MAPPER.readValue(response.getBody(), type);
        } catch (IOException e) {
            throw new MessageQueueException("Unable to consume.", e);
        }
    }
}
