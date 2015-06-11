package bo.gotthardt.model;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private String accessToken;

    /** The token's expiration date, after which it is no longer valid. */
    @Setter
    private LocalDateTime expirationDate;

    /** The user that this token authenticates. */
    @ManyToOne
    @JsonIgnore
    private User user;

    @CreatedTimestamp
    private LocalDateTime createdDate;

    /**
     * Constructor.
     * @param user The user that the token should authenticate.
     * @param duration How long the token should be valid for, starting from now.
     */
    public OAuth2AccessToken(User user, Duration duration) {
        // UUID is a convenient way to generate a random string.
        this.accessToken = UUID.randomUUID().toString();
        this.expirationDate = LocalDateTime.now().plus(duration);
        this.user = user;
    }

    public String loggableAccessToken() {
        return accessToken.substring(0, 8) + "-****-****-****-************";
    }

    /**
     * Returns whether this token is currently valid.
     */
    @JsonIgnore
    public boolean isValid() {
        return expirationDate.isAfter(LocalDateTime.now());
    }
}
