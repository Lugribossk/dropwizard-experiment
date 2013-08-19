package bo.gotthardt;

import org.bson.types.ObjectId;

/**
 * @author Bo Gotthardt
 */
public interface Persistable {
    ObjectId getId();
    void setId(ObjectId id);
}
