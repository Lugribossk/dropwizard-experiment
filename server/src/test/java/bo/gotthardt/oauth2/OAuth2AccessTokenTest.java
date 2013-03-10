package bo.gotthardt.oauth2;

import bo.gotthardt.model.User;
import bo.gotthardt.model.OAuth2AccessToken;
import org.joda.time.Duration;
import org.junit.Test;

import static bo.gotthardt.util.fest.DropwizardAssertions.assertThat;

/**
 * Tests for {@link bo.gotthardt.model.OAuth2AccessToken}.
 *
 * @author Bo Gotthardt
 */
public class OAuth2AccessTokenTest {
    @Test
    public void shouldBeValidWhenNotExpired() {
        User user = new User("test", "blah");
        OAuth2AccessToken token = new OAuth2AccessToken(user, Duration.standardHours(1));

        assertThat(token.isValid()).isTrue();
    }

    @Test
    public void shouldNotBeValidWhenExpired() {
        User user = new User("test", "blah");
        OAuth2AccessToken token = new OAuth2AccessToken(user, Duration.ZERO);

        assertThat(token.isValid()).isFalse();
    }
}
