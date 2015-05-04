package bo.gotthardt.model;

import bo.gotthardt.access.Feature;
import bo.gotthardt.access.Owned;
import bo.gotthardt.access.Principal;
import bo.gotthardt.rest.Persistable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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
    private EnumSet<Feature> features = EnumSet.noneOf(Feature.class); // TODO annotation?

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
        return this.features.contains(feature) || this.customer.hasAccessTo(feature);
    }

    public void addFeature(User adder, Feature feature) {
        adder.assertAccessTo(feature);
        this.features.add(feature);
    }

    public void removeFeature(Feature feature) {
        this.features.remove(feature);
    }
}
