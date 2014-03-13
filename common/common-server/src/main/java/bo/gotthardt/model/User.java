package bo.gotthardt.model;

import bo.gotthardt.AccessibleBy;
import bo.gotthardt.Persistable;
import bo.gotthardt.Principal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Bo Gotthardt
 */
@Entity
@Table(name = "users") // Avoid warnings about "user" being a reserved word.
@Getter
@Setter
@ToString(of = {"id", "username"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements Persistable, AccessibleBy<User>, Principal {
    @Id
    private long id;
    private String username;
    private String email;
    private String name;
    @Embedded
    @JsonIgnore
    private HashedValue password;

    public User(String username, String password) {
        this.username = username;
        this.password = new HashedValue(password);
    }

    @Override
    public boolean isAccessibleBy(User principal) {
        return id == principal.getId();
    }
}
