package bo.gotthardt.queue.rabbitmq;

import bo.gotthardt.queue.MessageQueue;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Bo Gotthardt
 */
@Slf4j
public class RabbitMQBundle implements ConfiguredBundle<HasRabbitMQConfiguration>, Managed {
    private final ConnectionFactory factory = new ConnectionFactory();
    @Getter(AccessLevel.PACKAGE)
    private Connection connection;
    @Getter(AccessLevel.PACKAGE)
    private Channel channel;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // Empty on purpose.
    }

    @Override
    public void run(HasRabbitMQConfiguration configuration, Environment environment) throws Exception {
        RabbitMQConfiguration rabbitMQ = configuration.getRabbitMQ();
        factory.setHost(rabbitMQ.getHost());
        factory.setUsername(rabbitMQ.getUsername());
        factory.setPassword(rabbitMQ.getPassword());
        factory.setPort(rabbitMQ.getPort());
        factory.setAutomaticRecoveryEnabled(true);

        log.info("Connecting to RabbitMQ on '{}' with username '{}'.", factory.getHost(), factory.getUsername());

        environment.lifecycle().manage(this);
        environment.healthChecks().register("rabbitmq", new RabbitMQHealthCheck(this));
    }

    @Override
    public void start() throws Exception {
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.basicQos(1);
    }

    @Override
    public void stop() throws Exception {
        if (connection != null) {
            connection.close();
        }
        if (channel != null) {
            channel.close();
        }
    }

    public MessageQueue getQueue(String queueName) {
        return new MessageQueue(channel, queueName);
    }
}
