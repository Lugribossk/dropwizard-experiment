package bo.gotthardt.queue;

import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link QueueWorker}.
 */
public class QueueWorkerTest {
    @Test
    public void shouldConsumeProcessingFunctionWhenRun() {
        @SuppressWarnings("unchecked")
        MessageQueue<String> queue = mock(MessageQueue.class);

        new TestWorker(queue).run();

        verify(queue).consume(Matchers.any());
    }

    private static class TestWorker extends QueueWorker<String> {
        public TestWorker(MessageQueue<String> queue) {
            super(queue);
        }

        @Override
        protected Void process(String message) {
            return null;
        }
    }
}