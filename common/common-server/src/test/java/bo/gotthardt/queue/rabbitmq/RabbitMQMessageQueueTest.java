package bo.gotthardt.queue.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import io.dropwizard.jackson.Jackson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.mockito.Matchers;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link bo.gotthardt.queue.rabbitmq.RabbitMQMessageQueue}.
 */
public class RabbitMQMessageQueueTest {
    private static final String QUEUE_NAME = "test";
    private Channel channel = mock(Channel.class);

    @Test
    public void shouldDeclareDurableQueue() throws IOException {
        RabbitMQMessageQueue<TestMsg> queue = new RabbitMQMessageQueue<TestMsg>(channel, QUEUE_NAME, TestMsg.class);

        verify(channel).queueDeclare(QUEUE_NAME, true, false, false, null);
    }

    @Test
    public void shouldPublishPersistentMessageToQueue() throws IOException {
        RabbitMQMessageQueue<TestMsg> queue = new RabbitMQMessageQueue<TestMsg>(channel, QUEUE_NAME, TestMsg.class);
        TestMsg message = new TestMsg("blah", 5);
        byte[] messageBytes = Jackson.newObjectMapper().writeValueAsBytes(message);

        queue.publish(message);

        verify(channel).basicPublish(Matchers.eq(""), Matchers.eq(QUEUE_NAME), Matchers.eq(MessageProperties.PERSISTENT_TEXT_PLAIN), Matchers.eq(messageBytes));
    }

    @Test
    public void shouldConsumeByAttachingConsumerToQueue() throws IOException {
        RabbitMQMessageQueue<TestMsg> queue = new RabbitMQMessageQueue<TestMsg>(channel, QUEUE_NAME, TestMsg.class);

        queue.consume(msg -> null);

        verify(channel).basicConsume(Matchers.eq(QUEUE_NAME), Matchers.eq(false), Matchers.any(FunctionConsumer.class));
    }

    @NoArgsConstructor
    private static class TestMsg {
        @Getter
        private String name;
        @Getter
        private int count;

        private TestMsg(String name, int count) {
            this.name = name;
            this.count = count;
        }
    }
}