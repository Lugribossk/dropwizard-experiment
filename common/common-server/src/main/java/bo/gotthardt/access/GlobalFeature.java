package bo.gotthardt.access;

import java.util.EnumSet;

public enum GlobalFeature implements Feature {
    BLAHGLOBAL;

    private static EnumSet<GlobalFeature> enabled = EnumSet.of(BLAHGLOBAL);

    public static boolean isEnabled(GlobalFeature feature) {
        return enabled.contains(feature);
    }
}
