package bo.gotthardt;

/**
 * @author Bo Gotthardt
 */
public interface AccessibleBy<T extends Principal> {
    public boolean isAccessibleBy(T principal);
}
