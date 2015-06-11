package bo.gotthardt.oauth2;

import bo.gotthardt.model.OAuth2AccessToken;
import bo.gotthardt.model.User;
import org.junit.Test;

import java.time.Duration;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * Tests for {@link bo.gotthardt.model.OAuth2AccessToken}.
 *
 * @author Bo Gotthardt
 */
public class OAuth2AccessTokenTest {
    @Test
    public void shouldBeValidWhenNotExpired() {
        User user = new User("test", "blah", "Blah");
        OAuth2AccessToken token = new OAuth2AccessToken(user, Duration.ofDays(1));

        assertThat(token.isValid()).isTrue();
    }

    @Test
    public void shouldNotBeValidWhenExpired() {
        User user = new User("test", "blah", "Blah");
        OAuth2AccessToken token = new OAuth2AccessToken(user, Duration.ZERO);

        assertThat(token.isValid()).isFalse();
    }
}
