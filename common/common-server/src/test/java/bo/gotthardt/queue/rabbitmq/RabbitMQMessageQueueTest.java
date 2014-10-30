package bo.gotthardt.queue.rabbitmq;

import com.codahale.metrics.MetricRegistry;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import io.dropwizard.jackson.Jackson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.mockito.Matchers;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * Tests for {@link bo.gotthardt.queue.rabbitmq.RabbitMQMessageQueue}.
 */
public class RabbitMQMessageQueueTest {
    private static final String QUEUE_NAME = "test";
    private Channel channel = mock(Channel.class);
    private MetricRegistry metrics = new MetricRegistry();

    @Test
    public void shouldDeclareDurableQueue() throws IOException {
        RabbitMQMessageQueue<TestMsg> queue = new RabbitMQMessageQueue<>(channel, QUEUE_NAME, TestMsg.class, metrics);

        verify(channel).queueDeclare(QUEUE_NAME, true, false, false, null);
    }

    @Test
    public void shouldPublishPersistentMessageToQueue() throws IOException {
        RabbitMQMessageQueue<TestMsg> queue = new RabbitMQMessageQueue<>(channel, QUEUE_NAME, TestMsg.class, metrics);
        TestMsg message = new TestMsg("blah", 5);
        byte[] messageBytes = Jackson.newObjectMapper().writeValueAsBytes(message);

        queue.publish(message);

        verify(channel).basicPublish(Matchers.eq(""), Matchers.eq(QUEUE_NAME), Matchers.eq(MessageProperties.PERSISTENT_TEXT_PLAIN), Matchers.eq(messageBytes));
    }

    @Test
    public void shouldConsumeByAttachingConsumerToQueue() throws IOException {
        RabbitMQMessageQueue<TestMsg> queue = new RabbitMQMessageQueue<>(channel, QUEUE_NAME, TestMsg.class, metrics);

        queue.consume(msg -> null);

        verify(channel).basicConsume(Matchers.eq(QUEUE_NAME), Matchers.eq(false), Matchers.any(FunctionConsumer.class));
    }

    @Test
    public void shouldRecordPublishMetrics() {
        RabbitMQMessageQueue<TestMsg> queue = new RabbitMQMessageQueue<>(channel, QUEUE_NAME, TestMsg.class, metrics);

        queue.publish(new TestMsg("blah", 5));

        assertThat(metrics.meter("queue.TestMsg.test.publish").getCount()).isEqualTo(1);
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