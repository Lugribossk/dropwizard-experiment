package bo.gotthardt.model;

import bo.gotthardt.access.Feature;
import bo.gotthardt.access.GloballyEnabledFeatures;
import bo.gotthardt.access.Principal;
import bo.gotthardt.rest.Persistable;
import lombok.*;

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
    private EnumSet<Feature> features = EnumSet.noneOf(Feature.class);

    public Customer(String name) {
        this.name = name;
    }

    @Override
    public boolean hasAccessTo(Feature feature) {
        return this.features.contains(feature) || GloballyEnabledFeatures.isEnabled(feature);
    }

    public void addFeature(User adder, Feature feature) {
        adder.assertAccessTo(feature);
        this.features.add(feature);
    }

    public void removeFeature(Feature feature) {
        this.features.remove(feature);
    }
}
