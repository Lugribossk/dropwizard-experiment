package bo.gotthardt.test;

import bo.gotthardt.model.Customer;
import bo.gotthardt.model.User;
import com.avaje.ebean.EbeanServer;

/**
 * @author Bo Gotthardt
 */
public class TestData {
    public static User createUser(EbeanServer db) {
        Customer customer = new Customer("Test Customer");
        db.save(customer);

        User user = new User("testuser", "testpassword", "Test User");
        user.setCustomer(customer);
        db.save(user);

        return user;
    }
}
