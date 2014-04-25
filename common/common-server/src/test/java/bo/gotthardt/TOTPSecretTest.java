package bo.gotthardt;

import org.joda.time.DateTime;
import org.junit.Test;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;


/**
 * @author Bo Gotthardt
 */
public class TOTPSecretTest {
    @Test
    public void shouldBeSameIn30SecondInterval() {
        TOTPSecret secret = new TOTPSecret();
        DateTime dateTime = DateTime.parse("1970-01-01T00:00:00Z");
        int token = secret.generateToken(dateTime);

        assertThat(token).isEqualTo(secret.generateToken(dateTime.plusSeconds(1)));
        assertThat(token).isEqualTo(secret.generateToken(dateTime.plusSeconds(15)));
        assertThat(token).isEqualTo(secret.generateToken(dateTime.plusSeconds(29)));
        assertThat(token).isNotEqualTo(secret.generateToken(dateTime.plusSeconds(30)));
    }

    @Test
    public void shouldAccepCurrentToken() {
        TOTPSecret secret = new TOTPSecret();
        DateTime dateTime = DateTime.parse("2014-01-01T00:00:00Z");

        int token = secret.generateToken(dateTime);

        assertThat(secret.isSameTokenAt(token, dateTime)).isTrue();
    }

    @Test
    public void shouldAcceptTokenFromPrevious30Seconds() {
        TOTPSecret secret = new TOTPSecret();
        DateTime dateTime = DateTime.parse("2014-01-01T00:00:00Z");

        int oldToken = secret.generateToken(dateTime.minusSeconds(30));
        int olderToken = secret.generateToken(dateTime.minusSeconds(31));

        assertThat(secret.isSameTokenAt(oldToken, dateTime)).isTrue();
        assertThat(secret.isSameTokenAt(olderToken, dateTime)).isFalse();
    }

    @Test
    public void shouldAcceptTokenFromNext30Seconds() {
        TOTPSecret secret = new TOTPSecret();
        DateTime dateTime = DateTime.parse("2014-01-01T00:00:00Z");

        int newToken = secret.generateToken(dateTime.plusSeconds(31));
        int newerToken = secret.generateToken(dateTime.plusSeconds(60));

        assertThat(secret.isSameTokenAt(newToken, dateTime)).isTrue();
        assertThat(secret.isSameTokenAt(newerToken, dateTime)).isFalse();
    }
}
