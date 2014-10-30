package bo.gotthardt.access;

import bo.gotthardt.exception.UnauthorizedException;
import bo.gotthardt.model.Customer;
import bo.gotthardt.model.User;
import org.junit.Test;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

public class OwnedTest {
    @Test
    public void shouldDetermineByUsersCustomer() {
        User user = new User("test", "test", "test");
        Customer customer = new Customer("test");
        Customer customer2 = new Customer("qwerty");
        user.setCustomer(customer);

        assertThat(new Blah().isOwnedBy(user)).isTrue();

        user.setCustomer(customer2);

        assertThat(new Blah().isOwnedBy(user)).isFalse();
    }

    @Test(expected = UnauthorizedException.class)
    public void shouldThrowOnMissingCustomerOwnership() {
        Customer customer2 = new Customer("qwerty");

        new Blah().assertOwnedBy(customer2);
    }

    @Test(expected = UnauthorizedException.class)
    public void shouldThrowOnMissingUserOwnership() {
        User user = new User("test", "test", "test");
        Customer customer2 = new Customer("qwerty");
        user.setCustomer(customer2);

        new Blah().assertOwnedBy(user);
    }

    private static class Blah implements Owned {
        @Override
        public boolean isOwnedBy(Customer customer) {
            return customer.getName().equals("test");
        }
    }
}
