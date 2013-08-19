package bo.gotthardt.model;

import bo.gotthardt.Persistable;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Widget implements Persistable {
    @Id
    private ObjectId id;

    @NotEmpty
    private String name;

    public Widget(String name) {
        this.name = name;
    }
}
