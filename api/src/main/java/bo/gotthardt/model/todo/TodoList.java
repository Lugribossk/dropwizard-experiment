package bo.gotthardt.model.todo;

import bo.gotthardt.AccessibleBy;
import bo.gotthardt.Persistable;
import bo.gotthardt.model.User;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoList implements Persistable, AccessibleBy<User> {
    @Id
    private long id;
    private String name;
    @OneToMany
    private List<TodoItem> items = Lists.newArrayList();
    private DateTime creationDate = DateTime.now();
    private User owner;

    public TodoList(String name) {
        this.name = name;
    }

    @Override
    public boolean isAccessibleBy(User principal) {
        return owner.getId() == principal.getId();
    }
}
