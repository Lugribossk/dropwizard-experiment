package bo.gotthardt.queue.rabbitmq;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
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
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final Function<T, Void> processor;
    private final Class<T> type;
    private final Timer duration;
    private final Meter success;
    private final Meter failure;

    FunctionConsumer(Channel channel, Function<T, Void> processor, Class<T> type, String name, MetricRegistry metrics) {
        super(channel);
        this.processor = processor;
        this.type = type;
        this.duration = metrics.timer(MetricRegistry.name("queue", type.getSimpleName(), name, "consume", "duration"));
        this.success = metrics.meter(MetricRegistry.name("queue", type.getSimpleName(), name, "consume", "success"));
        this.failure = metrics.meter(MetricRegistry.name("queue", type.getSimpleName(), name, "consume", "failure"));
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        long deliveryTag = envelope.getDeliveryTag();

        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            T message = MAPPER.readValue(body, type);

            if (log.isTraceEnabled()) {
                log.trace("Received message '{}' with data '{}'.", deliveryTag, new String(body));
            }

            processor.apply(message);
            getChannel().basicAck(deliveryTag, false);

            success.mark();
            log.info("Processed {} message '{}' succesfully in {} ms.", type.getSimpleName(), deliveryTag, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            getChannel().basicNack(deliveryTag, false, true);

            failure.mark();
            log.error("Processing {} message '{}' failed with exception:", type.getSimpleName(), deliveryTag, e);
        } finally {
            duration.update(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
        }
    }
}
