package bo.gotthardt;

import java.security.Principal;

/**
 * @author Bo Gotthardt
 */
public interface AccessibleBy<T extends Principal> {
    public boolean isAccessibleBy(T principal);
}
