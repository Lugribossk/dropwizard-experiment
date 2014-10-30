package bo.gotthardt.queue.rabbitmq;

import com.codahale.metrics.health.HealthCheck;
import lombok.RequiredArgsConstructor;

/**
 * Health check for RabbitMQ connection.
 *
 * @author Bo Gotthardt
 */
@RequiredArgsConstructor
class RabbitMQHealthCheck extends HealthCheck {
    private final RabbitMQBundle rabbitMQ;

    @Override
    protected Result check() throws Exception {
        if (rabbitMQ.getConnection().isOpen()) {
            return Result.healthy();
        } else {
            return Result.unhealthy("Connection not open");
        }
    }
}
