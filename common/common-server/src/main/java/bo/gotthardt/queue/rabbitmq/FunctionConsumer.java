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

    private final Meter successCount;
    private final Timer successDuration;
    private final Meter failureCount;
    private final Timer failureDuration;

    FunctionConsumer(Channel channel, Function<T, Void> processor, Class<T> type, String name, MetricRegistry metrics) {
        super(channel);
        this.processor = processor;
        this.type = type;

        String metricPrefix = "queue." + type.getSimpleName() + "." + name + ".consume";
        this.successCount = metrics.meter(MetricRegistry.name(metricPrefix, "success", "count"));
        this.successDuration = metrics.timer(MetricRegistry.name(metricPrefix, "success", "duration"));
        this.failureCount = metrics.meter(MetricRegistry.name(metricPrefix, "failure", "count"));
        this.failureDuration = metrics.timer(MetricRegistry.name(metricPrefix, "failure", "duration"));
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

            stopwatch.stop();
            successCount.mark();
            successDuration.update(stopwatch.elapsed(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
            log.info("Processed {} message '{}' succesfully in {} ms.", type.getSimpleName(), deliveryTag, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            getChannel().basicNack(deliveryTag, false, true);

            stopwatch.stop();
            failureCount.mark();
            failureDuration.update(stopwatch.elapsed(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
            log.error("Processing {} message '{}' failed with exception:", type.getSimpleName(), deliveryTag, e);
        }
    }
}
