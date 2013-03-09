package bo.gotthardt.model;

import bo.gotthardt.Persistable;
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
}
