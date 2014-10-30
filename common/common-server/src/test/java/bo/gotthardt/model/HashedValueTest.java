package bo.gotthardt.model;

import org.junit.Test;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class HashedValueTest {
    // TODO Move to integration tests since it's slow?

    @Test
    public void shouldEqualOriginalPlaintext() {
        HashedValue hashed = new HashedValue("test");

        assertThat(hashed.equalsPlaintext("test")).isTrue();
    }

    @Test
    public void shouldNotEqualDifferentPlaintext() {
        HashedValue hashed = new HashedValue("test");

        assertThat(hashed.equalsPlaintext("nottest")).isFalse();
    }
}
