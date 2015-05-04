package bo.gotthardt.access;

import bo.gotthardt.exception.UnauthorizedException;
import org.junit.Test;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

public class PrincipalTest {
    @Test
    public void shouldCheckAllFeaturesSpecified() {
        assertThat(new TestPrincipal().hasAccessToAll(Feature.BLAH1, Feature.BLAH2)).isTrue();
        assertThat(new TestPrincipal().hasAccessToAll(Feature.BLAH1, Feature.BLAH2, Feature.BLAH3)).isFalse();
    }

    @Test(expected = UnauthorizedException.class)
    public void shouldThrowOnMissingFeature() {
        new TestPrincipal().assertAccessTo(Feature.BLAH3);
    }

    @Test(expected = UnauthorizedException.class)
    public void shouldThrowOnMissingFeature2() {
        new TestPrincipal().assertAccessToAll(Feature.BLAH1, Feature.BLAH3);
    }

    private static class TestPrincipal implements Principal {
        @Override
        public boolean hasAccessTo(Feature feature) {
            return feature.equals(Feature.BLAH1) || feature.equals(Feature.BLAH2);
        }
    }
}
