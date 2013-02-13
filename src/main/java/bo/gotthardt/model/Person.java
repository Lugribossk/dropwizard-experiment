package bo.gotthardt.model;

import bo.gotthardt.Persistable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String name;
}
