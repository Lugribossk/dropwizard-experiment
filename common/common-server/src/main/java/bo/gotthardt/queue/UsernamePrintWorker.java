package bo.gotthardt.queue;

import bo.gotthardt.model.User;
import com.google.inject.name.Named;

import javax.inject.Inject;

/**
 * @author Bo Gotthardt
 */
public class UsernamePrintWorker extends QueueWorker<User> {

    @Inject
    public UsernamePrintWorker(@Named("username") MessageQueue<User> queue) {
        super(User.class, queue);
    }

    @Override
    protected void process(User message) {
        System.out.println("Username: " + message.getUsername());
    }
}
