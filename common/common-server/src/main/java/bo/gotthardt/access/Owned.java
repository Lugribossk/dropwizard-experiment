package bo.gotthardt.access;


import bo.gotthardt.model.Customer;
import bo.gotthardt.model.User;

/**
 *
 */
public interface Owned {
    public boolean isOwnedBy(Customer customer);

    public default boolean isOwnedBy(User user) {
        return isOwnedBy(user.getCustomer());
    }

    public default void assertOwnedBy(Customer customer) {
        AccessAssertions.assertOwnedBy(this, customer);
    }

    public default void assertOwnedBy(User user) {
        AccessAssertions.assertOwnedBy(this, user);
    }
}
