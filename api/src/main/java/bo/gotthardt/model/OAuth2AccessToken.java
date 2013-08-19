package bo.gotthardt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.UUID;

/**
 * OAuth2 access token.
 * <br/><br/>
 * This is the value added in a header on all requests to prove that they have been sent by a particular user.
 *
 * @author Bo Gotthardt
 */
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2AccessToken {
    /** The access token itself. */
    @Id
    private ObjectId id;

    private String accessToken;

    /** The token's expiration date, after which it is no longer valid. */
    @Setter
    private DateTime expirationDate;

    /** The user that this token authenticates. */
    @Reference
    @JsonIgnore
    private User user;

    /**
     * Constructor.
     * @param user The user that the token should authenticate.
     * @param duration How long the token should be valid for, starting from now.
     */
    public OAuth2AccessToken(User user, Duration duration) {
        // UUID is a convenient way to generate a random string.
        this.accessToken = UUID.randomUUID().toString();
        this.expirationDate = DateTime.now().plus(duration);
        this.user = user;
    }

    /**
     * Returns whether this token is currently valid.
     */
    public boolean isValid() {
        return expirationDate.isAfterNow();
    }
}
