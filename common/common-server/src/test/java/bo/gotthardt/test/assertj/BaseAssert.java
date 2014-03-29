package bo.gotthardt.test.assertj;

import org.assertj.core.api.AbstractAssert;

public class BaseAssert<S extends AbstractAssert<S, A>, A> extends AbstractAssert<S, A> {

    protected BaseAssert(A actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Compare the specified actual and expected values, and throw a ComparisonFailure if they are not equal.
     * The type of the values are Strings to allow us to use ComparisonFailure for IDE integration.
     *
     * @param actual the actual value
     * @param expected the expected value
     * @param message the message to show on failure
     */
    protected static void compare(String actual, String expected, String message) {
        if (actual == null || !actual.equals(expected)) {
            throw new junit.framework.ComparisonFailure(message, expected, actual);
        }
    }
}
