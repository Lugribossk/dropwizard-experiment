package bo.gotthardt.queue.rabbitmq;

import bo.gotthardt.queue.MessageQueue;
import com.google.common.base.Preconditions;
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
    private Channel channel;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // Empty on purpose.
    }

    @Override
    public void run(HasRabbitMQConfiguration configuration, Environment environment) throws Exception {
        RabbitMQConfiguration rabbitMQ = configuration.getRabbitMq();
        factory.setHost(rabbitMQ.getHost());
        factory.setUsername(rabbitMQ.getUsername());
        factory.setPassword(rabbitMQ.getPassword());
        factory.setPort(rabbitMQ.getPort());
        factory.setAutomaticRecoveryEnabled(true);

        log.info("Connecting to RabbitMQ on '{}' with username '{}'.", factory.getHost(), factory.getUsername());

        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.basicQos(1);

        environment.lifecycle().manage(this);
        environment.healthChecks().register("rabbitmq", new RabbitMQHealthCheck(this));
    }

    @Override
    public void start() throws Exception {
        // Connection and channel init code should really be in here, but it has to be earlier in the startup to be ready when Guice initializes.
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

    public <T> MessageQueue<T> getQueue(String queueName) {
        Preconditions.checkNotNull(channel, "Channel not initialized.");
        return new RabbitMQMessageQueue<>(channel, queueName);
    }
}
