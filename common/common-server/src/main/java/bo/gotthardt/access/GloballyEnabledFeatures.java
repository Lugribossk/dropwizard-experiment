package bo.gotthardt.access;

import java.util.EnumSet;
import java.util.Set;

public class GloballyEnabledFeatures {
    private static EnumSet<Feature> enabled = EnumSet.of(Feature.BLAH1);

    public static boolean isEnabled(Feature feature) {
        return enabled.contains(feature);
    }

    public static Set<Feature> getEnabled() {
        // TODO immutable copy
        return enabled;
    }
}
