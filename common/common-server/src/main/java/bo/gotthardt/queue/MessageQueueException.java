package bo.gotthardt.queue;

/**
 * @author Bo Gotthardt
 */
public class MessageQueueException extends RuntimeException {
    public MessageQueueException(Throwable t) {
        super(t);
    }

    public MessageQueueException(String message, Throwable t) {
        super(message, t);
    }
}
