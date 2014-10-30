package bo.gotthardt.queue.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import lombok.Getter;

/**
 * Configuration for {@link bo.gotthardt.queue.rabbitmq.RabbitMQBundle}.
 *
 * @author Bo Gotthardt
 */
@Getter
public class RabbitMQConfiguration {
    private String host = ConnectionFactory.DEFAULT_HOST;
    private String virtualHost = ConnectionFactory.DEFAULT_VHOST;
    private int port = ConnectionFactory.DEFAULT_AMQP_PORT;
    private String username = ConnectionFactory.DEFAULT_USER;
    private String password = ConnectionFactory.DEFAULT_PASS;
}
