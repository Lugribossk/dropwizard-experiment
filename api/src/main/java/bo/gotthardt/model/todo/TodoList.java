package bo.gotthardt.model.todo;

import bo.gotthardt.AccessibleBy;
import bo.gotthardt.Persistable;
import bo.gotthardt.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

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
    private ObjectId id;
    private String name;
    @Reference
    private List<TodoItem> items = Lists.newArrayList();
    private DateTime creationDate = DateTime.now();
    @Reference
    @JsonIgnore
    private User owner;

    public TodoList(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public void addItem(String name) {
        items.add(new TodoItem(name));
    }

    @Override
    public boolean isAccessibleBy(User principal) {
        return owner.getId() == principal.getId();
    }
}
