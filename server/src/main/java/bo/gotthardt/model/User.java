package bo.gotthardt.model;

import bo.gotthardt.HasAccessTo;
import bo.gotthardt.Persistable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@ToString(of = {"id", "username"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements Persistable, HasAccessTo<User> {
    @Id
    private long id;
    private String username;
    @Embedded
    @JsonIgnore
    private HashedValue password;

    public User(String username, String password) {
        this.username = username;
        this.password = new HashedValue(password);
    }

    @Override
    public boolean hasAccessTo(User possiblyAccessible) {
        return id == possiblyAccessible.getId();
    }
}
