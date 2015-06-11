package bo.gotthardt.model.todo;

import bo.gotthardt.AccessibleBy;
import bo.gotthardt.Persistable;
import bo.gotthardt.model.User;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoList implements Persistable, AccessibleBy<User> {
    @Id
    private UUID id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TodoItem> items = Lists.newArrayList();
    @CreatedTimestamp
    private LocalDateTime createdDate;
    @ManyToOne
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
        return owner.getId().equals(principal.getId());
    }
}
