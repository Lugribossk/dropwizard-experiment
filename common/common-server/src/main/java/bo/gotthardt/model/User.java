package bo.gotthardt.model;

import bo.gotthardt.rest.Persistable;
import bo.gotthardt.access.CustomerFeature;
import bo.gotthardt.access.Feature;
import bo.gotthardt.access.GlobalFeature;
import bo.gotthardt.access.Owned;
import bo.gotthardt.access.Principal;
import bo.gotthardt.access.UserFeature;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.EnumSet;

/**
 * A user that can log into the system.
 *
 * @author Bo Gotthardt
 */
@Entity
@Table(name = "users") // Avoid warnings about "user" being a reserved word.
@Getter
@Setter
@ToString(of = {"id", "username"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements Persistable, Owned, Principal {
    @Id
    private long id;
    private String username;
    private String email;
    private String name;
    @Embedded
    @JsonIgnore
    private HashedValue password;
    @ManyToOne
    @JsonIgnore
    private Customer customer;
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private EnumSet<UserFeature> features = EnumSet.noneOf(UserFeature.class); // TODO annotation?

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = new HashedValue(password);
        this.name = fullName;
    }

    @Override
    public boolean isOwnedBy(Customer customer) {
        return this.customer.getId() == customer.getId();
    }

    @Override
    public boolean hasAccessTo(Feature feature) {
        if (feature instanceof UserFeature) {
            return features.contains(feature);
        } else if (feature instanceof CustomerFeature || feature instanceof GlobalFeature) {
            return customer.hasAccessTo(feature);
        } else {
            throw new IllegalStateException("Feature inheritance hierarchy mismatch.");
        }
    }

    public void addFeature(User adder, UserFeature feature) {
        adder.assertAccessTo(feature);
        features.add(feature);
    }

    public void removeFeature(UserFeature feature) {
        features.remove(feature);
    }
}
