package bo.gotthardt.model;

import com.avaje.ebean.Ebean;

/**
 * @author Bo Gotthardt
 */
public abstract class Persistable {
    public abstract long getId();
    public abstract void setId(long id);

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
