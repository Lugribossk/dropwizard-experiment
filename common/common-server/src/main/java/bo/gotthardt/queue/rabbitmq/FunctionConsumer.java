package bo.gotthardt.queue.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.dropwizard.jackson.Jackson;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * A RabbitMQ {@link com.rabbitmq.client.Consumer} that maps messages to a specific type and runs a processing function on them.
 *
 * @author Bo Gotthardt
 */
@Slf4j
class FunctionConsumer<T> extends DefaultConsumer {
    private static ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final Function<T, Void> processor;
    private final Class<T> type;

    FunctionConsumer(Channel channel, Function<T, Void> processor, Class<T> type) {
        super(channel);
        this.processor = processor;
        this.type = type;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        long deliveryTag = envelope.getDeliveryTag();

        try {
            T message = MAPPER.readValue(body, type);

            log.trace("Received message '{}' with data '{}'.", deliveryTag, new String(body));
            Stopwatch timer = Stopwatch.createStarted();

            processor.apply(message);
            timer.stop();

            getChannel().basicAck(deliveryTag, false);
            log.info("Processed {} message '{}' succesfully in {} ms.", type.getSimpleName(), deliveryTag, timer.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            log.warn("Processing message failed with exception:", e);
            getChannel().basicNack(deliveryTag, false, true);
        }
    }
}
