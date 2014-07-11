package bo.gotthardt.queue.rabbitmq;

import bo.gotthardt.queue.MessageQueue;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Integration tests for {@link bo.gotthardt.queue.rabbitmq.RabbitMQBundle}.
 *
 * @author Bo Gotthardt
 */
public class RabbitMqIntegrationTest {
    private RabbitMQBundle bundle;
    private MessageQueue<TestMsg> queue;

    @Before
    public void setup() throws Exception {
        Environment environment = mock(Environment.class, Mockito.RETURNS_DEEP_STUBS);
        bundle = new RabbitMQBundle();

        bundle.run(new TestConfig(), environment);
        queue = createQueue("integrationtestqueue");
    }

    @After
    public void teardown() throws Exception {
        if (bundle != null) {
            bundle.stop();
        }
    }

    @Test
    public void shouldQueueAndConsumeMessages() {
        queue.publish(new TestMsg("testname"));
        TestMsg msg = queue.consumeNext();

        assertThat(msg.getName()).isEqualTo("testname");
    }

    @Test
    public void shouldGetMessageFromDifferentQueueWithSameName() {
        MessageQueue<TestMsg> queue2 = createQueue("integrationtestqueue");

        queue.publish(new TestMsg("testname"));

        assertThat(queue).isNotSameAs(queue2);
        assertThat(queue2.consumeNext().getName()).isEqualTo("testname");
    }

    @Test
    public void shouldNotGetMessageFromQueueWithDifferentName() {
        MessageQueue<TestMsg> queue2 = createQueue("integrationtestqueue2");

        queue.publish(new TestMsg("testname"));
        queue2.publish(new TestMsg("testname2"));

        assertThat(queue.consumeNext().getName()).isEqualTo("testname");
        assertThat(queue2.consumeNext().getName()).isEqualTo("testname2");
    }

    private MessageQueue<TestMsg> createQueue(String name) {
        MessageQueue<TestMsg> queue = bundle.getQueue(name, TestMsg.class);
        // Delete all messages in the queue if any happen to be left over from a failed test run.
        bundle.purgeQueue(queue);
        return queue;
    }

    private static class TestConfig implements HasRabbitMQConfiguration {
        @Getter
        private RabbitMQConfiguration rabbitMq = new RabbitMQConfiguration();
    }

    @NoArgsConstructor
    private static class TestMsg {
        @Getter
        private String name;

        private TestMsg(String name) {
            this.name = name;
        }
    }
}
