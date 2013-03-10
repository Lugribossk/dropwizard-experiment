package bo.gotthardt;

/**
 * @author Bo Gotthardt
 */
public interface HasAccessTo<T> {
    public boolean hasAccessTo(T possiblyAccessible);
}
