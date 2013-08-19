package bo.gotthardt.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

/**
 * A securely hashed value suitable for storing passwords.
 *
 * @see <a href="http://codahale.com/how-to-safely-store-a-password/">How To Safely Store A Password</a>
 * @author Bo Gotthardt
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HashedValue {
    private String hashedValue;

    /**
     * Constructor.
     * @param plaintext The plaintext value to hash.
     */
    public HashedValue(String plaintext) {
        this.hashedValue = BCrypt.hashpw(plaintext, BCrypt.gensalt(12));
    }

    /**
     * Returns whether this is equal to the specified plaintext value.
     * @param plaintext The plaintext.
     */
    public boolean equalsPlaintext(String plaintext) {
        return BCrypt.checkpw(plaintext, hashedValue);
    }
}
