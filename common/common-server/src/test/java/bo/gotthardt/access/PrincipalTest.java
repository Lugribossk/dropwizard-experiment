package bo.gotthardt.access;

import bo.gotthardt.exception.UnauthorizedException;
import org.junit.Test;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

public class PrincipalTest {
    @Test
    public void shouldCheckAllFeaturesSpecified() {
        assertThat(new Blah().hasAccessToAll(TestFeatures.BLAH1, TestFeatures.BLAH2)).isTrue();
        assertThat(new Blah().hasAccessToAll(TestFeatures.BLAH1, TestFeatures.BLAH2, TestFeatures.BLAH3)).isFalse();
    }

    @Test(expected = UnauthorizedException.class)
    public void shouldThrowOnMissingFeature() {
        new Blah().assertAccessTo(TestFeatures.BLAH3);
    }

    @Test(expected = UnauthorizedException.class)
    public void shouldThrowOnMissingFeature2() {
        new Blah().assertAccessToAll(TestFeatures.BLAH1, TestFeatures.BLAH3);
    }

    private static class Blah implements Principal {
        @Override
        public boolean hasAccessTo(Feature feature) {
            return feature.equals(TestFeatures.BLAH1) || feature.equals(TestFeatures.BLAH2);
        }
    }

    private static enum TestFeatures implements Feature {
        BLAH1,
        BLAH2,
        BLAH3
    }
}
