package bo.gotthardt.model.todo;

import bo.gotthardt.AccessibleBy;
import bo.gotthardt.Persistable;
import bo.gotthardt.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

/**
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoItem implements Persistable, AccessibleBy<User> {
    @Id
    private ObjectId id;
    private String name;
    private boolean completed = false;
    @Reference
    @JsonIgnore
    private TodoList todoList;

    public TodoItem(String name) {
        this.name = name;
    }

    @Override
    public boolean isAccessibleBy(User principal) {
        return todoList.getOwner().getId() == principal.getId();
    }
}
