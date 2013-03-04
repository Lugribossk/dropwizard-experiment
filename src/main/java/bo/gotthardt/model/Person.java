package bo.gotthardt.model;

import com.avaje.ebean.Ebean;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Person implements Persistable {
    @Id
    private long id;

    @NotEmpty
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public void save() {
        Ebean.save(this);
    }

    public void update(long id) {
        this.setId(id);
        Ebean.update(this);
    }

    public void refresh() {
        Ebean.refresh(this);
    }
}
