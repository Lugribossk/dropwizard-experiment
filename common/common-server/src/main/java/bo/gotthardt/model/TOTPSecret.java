package bo.gotthardt.model;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.common.net.UrlEscapers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Embeddable;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

/**
 * A secret key for Time-based One-Time Passwords.
 * Can be encoded for sending to a client or verify a token from a client.
 *
 * @see <a href="http://jacob.jkrall.net/totp/">Beginner's Guide to TOTP</a>
 * @author Bo Gotthardt
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TOTPSecret {
    private byte[] key;

    TOTPSecret(byte[] key) {
        this.key = key;
    }

    /**
     * Get the encoded version of the key, suitable for sending to a client application.
     * @param issuer The issuer name
     * @param accountName The key's user's account name
     */
    public String getEncoded(String issuer, String accountName) {
        // See https://code.google.com/p/google-authenticator/wiki/KeyUriFormat
        Preconditions.checkArgument(!issuer.contains(":"));
        Preconditions.checkArgument(!accountName.contains(":"));

        Function<String, String> escape = UrlEscapers.urlFormParameterEscaper().asFunction();

        return String.format("otpauth://totp/%1$s:%2$s?secret=%3$s&issuer=%1$s",
                      escape.apply(issuer),
                      escape.apply(accountName),
                      BaseEncoding.base32().encode(key).toUpperCase());
    }

    /**
     * Returns whether the specified token is correct (i.e. identical to the one generated from the key) at the specified time.
     * Also allows the token for the previous and next time steps.
     * @param token The token
     * @param time The time
     */
    public boolean isCorrectTokenAt(int token, DateTime time) {
        int previousToken = generateToken(time.minusSeconds(30));
        int currentToken = generateToken(time);
        int nextToken = generateToken(time.plusSeconds(30));

        return token == previousToken || token == currentToken || token == nextToken;
    }

    /**
     * Generate a token for the specified time.
     * (Technically the specified time's time step.)
     * @param time The time
     */
    public int generateToken(DateTime time) {
        return tokenAt(time.getMillis() / 30000);
    }

    /**
     * Generate a token for the exact specified time.
     * @param time The time, as milliseconds since the Unix epoch
     */
    private int tokenAt(long time) {
        byte[] data = ByteBuffer.allocate(8).putLong(time).array();

        // Borrowed from https://github.com/wstrange/GoogleAuth/blob/master/src/main/java/com/warrenstrange/googleauth/GoogleAuthenticator.java

        // Building the secret key specification for the HmacSHA1 algorithm.
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        try {
            // Getting an HmacSHA1 algorithm implementation from the JCE.
            Mac mac = Mac.getInstance("HmacSHA1");

            // Initializing the MAC algorithm.
            mac.init(signKey);

            // Processing the instant of time and getting the encrypted data.
            byte[] hash = mac.doFinal(data);

            // Building the validation code.
            int offset = hash[20 - 1] & 0xF;

            // We are using a long because Java hasn't got an unsigned integer type.
            long truncatedHash = 0;

            for (int i = 0; i < 4; ++i) {
                //truncatedHash = (truncatedHash * 256) & 0xFFFFFFFF;
                truncatedHash <<= 8;

                // Java bytes are signed but we need an unsigned one:
                // cleaning off all but the LSB.
                truncatedHash |= (hash[offset + i] & 0xFF);
            }

            // Cleaning bits higher than 32nd and calculating the module with the
            // maximum validation code value.
            truncatedHash &= 0x7FFFFFFF;
            truncatedHash %= 1000 * 1000;

            return (int) truncatedHash;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new TOTPSecret with a random key.
     */
    public static TOTPSecret create() {
        byte[] key = new byte[10];
        new SecureRandom().nextBytes(key);

        return new TOTPSecret(key);
    }
}
