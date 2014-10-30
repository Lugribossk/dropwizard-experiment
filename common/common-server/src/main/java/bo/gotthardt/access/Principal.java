package bo.gotthardt.access;

/**
 * A principal is an "actor" that has access to features, e.g. a user having access to edit documents.
 */
public interface Principal {
    public boolean hasAccessTo(Feature feature);

    public default boolean hasAccessToAll(Feature... features) {
        for (Feature feature : features) {
            if (!hasAccessTo(feature)) {
                return false;
            }
        }
        return true;
    }

    public default void assertAccessTo(Feature feature) {
        AccessAssertions.assertAccessTo(this, feature);
    }

    public default void assertAccessToAll(Feature... features) {
        for (Feature feature : features) {
            assertAccessTo(feature);
        }
    }
}
