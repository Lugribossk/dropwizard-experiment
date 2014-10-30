package bo.gotthardt.model;

import bo.gotthardt.rest.Persistable;
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
public class Widget implements Persistable {
    @Id
    private long id;

    @NotEmpty
    private String name;

    public Widget(String name) {
        this.name = name;
    }
}
