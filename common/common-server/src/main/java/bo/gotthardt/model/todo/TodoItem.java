package bo.gotthardt.model.todo;

import bo.gotthardt.rest.Persistable;
import bo.gotthardt.access.Owned;
import bo.gotthardt.model.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoItem implements Persistable, Owned {
    @Id
    private long id;
    private String name;
    private boolean completed = false;
    @ManyToOne
    @JsonIgnore
    private TodoList todoList;

    public TodoItem(String name) {
        this.name = name;
    }

    @Override
    public boolean isOwnedBy(Customer customer) {
        return todoList.isOwnedBy(customer);
    }
}
