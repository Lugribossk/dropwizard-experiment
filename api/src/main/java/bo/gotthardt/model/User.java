package bo.gotthardt.model;

import bo.gotthardt.AccessibleBy;
import bo.gotthardt.Persistable;
import bo.gotthardt.Principal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

/**
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@ToString(of = {"id", "username"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements Persistable, AccessibleBy<User>, Principal {
    @Id
    private ObjectId id;
    private String username;
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
