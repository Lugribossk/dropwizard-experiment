package bo.gotthardt.model;

import bo.gotthardt.AccessibleBy;
import bo.gotthardt.Persistable;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private UUID id;
    private String username;
    private String email;
    private String name;
    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "hashedValue", column = @Column(name = "password"))})
    @JsonIgnore
    private HashedValue password;
    @CreatedTimestamp
    private LocalDateTime createdDate;
    @UpdatedTimestamp
    private LocalDateTime updatedDate;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = new HashedValue(password);
        this.name = fullName;
    }

    @Override
    public boolean isAccessibleBy(User principal) {
        return id.equals(principal.getId());
    }
}
