package bo.gotthardt.model;

import com.avaje.ebean.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Bo Gotthardt
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerification {
    @Id
    private String token;

    @Setter
    private LocalDateTime expirationDate;

    private Type type;

    @ManyToOne
    @JsonIgnore
    private User user;

    public EmailVerification(User user, Duration duration, Type type) {
        this.token = UUID.randomUUID().toString();
        this.expirationDate = LocalDateTime.now().plus(duration);
        this.user = user;
        this.type = type;
    }

    @JsonIgnore
    public boolean isValid() {
        return expirationDate.isAfter(LocalDateTime.now());
    }

    public String getUrl() {
        return "http://localhost:8080/#verify/" + this.token;
    }

    public static enum Type {
        @EnumValue("P")
        PASSWORD_RESET
    }
}
