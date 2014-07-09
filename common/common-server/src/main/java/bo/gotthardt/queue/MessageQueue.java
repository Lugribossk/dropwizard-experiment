package bo.gotthardt.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import io.dropwizard.jackson.Jackson;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Bo Gotthardt
 */
@Slf4j
public class MessageQueue<T> {
    private static ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Getter(AccessLevel.PACKAGE)
    private final Channel channel;
    @Getter
    private final String name;

    protected MessageQueue(Channel channel, String name) {
        this.channel = channel;
        this.name = name;
        try {
            channel.queueDeclare(name, true, false, false, null);
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

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
}
