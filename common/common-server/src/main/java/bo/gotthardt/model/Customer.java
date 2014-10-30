package bo.gotthardt.model;

import bo.gotthardt.rest.Persistable;
import bo.gotthardt.access.CustomerFeature;
import bo.gotthardt.access.Feature;
import bo.gotthardt.access.GlobalFeature;
import bo.gotthardt.access.Principal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Entity
@ToString(of = {"id", "name"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer implements Persistable, Principal {
    @Id
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();
    private EnumSet<CustomerFeature> features = EnumSet.noneOf(CustomerFeature.class);

    public Customer(String name) {
        this.name = name;
    }

    @Override
    public boolean hasAccessTo(Feature feature) {
        if (feature instanceof CustomerFeature) {
            return features.contains(feature);
        } else if (feature instanceof GlobalFeature) {
            return GlobalFeature.isEnabled((GlobalFeature) feature);
        } else {
            throw new IllegalStateException("Feature inheritance hierarchy mismatch.");
        }
    }

    public void addFeature(User adder, CustomerFeature feature) {
        adder.assertAccessTo(feature);
        features.add(feature);
    }

    public void removeFeature(CustomerFeature feature) {
        features.remove(feature);
    }
}
