package bo.gotthardt.model;

import bo.gotthardt.model.TOTPSecret;
import org.joda.time.DateTime;
import org.junit.Test;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;


/**
 * Tests for {@link bo.gotthardt.model.TOTPSecret}.
 */
public class TOTPSecretTest {
    @Test
    public void shouldBeSameIn30SecondInterval() {
        TOTPSecret secret = TOTPSecret.create();
        DateTime dateTime = DateTime.parse("1970-01-01T00:00:00Z");
        int token = secret.generateToken(dateTime);

        assertThat(token).isEqualTo(secret.generateToken(dateTime.plusSeconds(1)));
        assertThat(token).isEqualTo(secret.generateToken(dateTime.plusSeconds(15)));
        assertThat(token).isEqualTo(secret.generateToken(dateTime.plusSeconds(29)));
        assertThat(token).isNotEqualTo(secret.generateToken(dateTime.plusSeconds(30)));
    }

    @Test
    public void shouldAccepCurrentToken() {
        TOTPSecret secret = TOTPSecret.create();
        DateTime dateTime = DateTime.parse("2014-01-01T00:00:00Z");

        int token = secret.generateToken(dateTime);

        assertThat(secret.isCorrectTokenAt(token, dateTime)).isTrue();
    }

    @Test
    public void shouldAcceptTokenFromPrevious30Seconds() {
        TOTPSecret secret = TOTPSecret.create();
        DateTime dateTime = DateTime.parse("2014-01-01T00:00:00Z");

        int oldToken = secret.generateToken(dateTime.minusSeconds(30));
        int olderToken = secret.generateToken(dateTime.minusSeconds(31));

        assertThat(secret.isCorrectTokenAt(oldToken, dateTime)).isTrue();
        assertThat(secret.isCorrectTokenAt(olderToken, dateTime)).isFalse();
    }

    @Test
    public void shouldAcceptTokenFromNext30Seconds() {
        TOTPSecret secret = TOTPSecret.create();
        DateTime dateTime = DateTime.parse("2014-01-01T00:00:00Z");

        int newToken = secret.generateToken(dateTime.plusSeconds(31));
        int newerToken = secret.generateToken(dateTime.plusSeconds(60));

        assertThat(secret.isCorrectTokenAt(newToken, dateTime)).isTrue();
        assertThat(secret.isCorrectTokenAt(newerToken, dateTime)).isFalse();
    }

    @Test
    public void shouldEncodeToken() {
        byte[] key = "1234567890".getBytes();
        TOTPSecret secret = new TOTPSecret(key);

        assertThat(secret.getEncoded("test", "test@user"))
            .isEqualTo("otpauth://totp/test:test%40user?secret=GEZDGNBVGY3TQOJQ&issuer=test");
    }
}
