package bo.gotthardt.todolist.worker;

import bo.gotthardt.model.User;
import bo.gotthardt.queue.MessageQueue;
import bo.gotthardt.queue.QueueWorker;
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
    protected Void process(User message) {
        System.out.println("Username: " + message.getUsername());
        return null;
    }
}
