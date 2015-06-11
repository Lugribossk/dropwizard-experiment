package bo.gotthardt;

import java.util.UUID;

/**
 * @author Bo Gotthardt
 */
public interface Persistable {
    UUID getId();
    void setId(UUID id);
}
