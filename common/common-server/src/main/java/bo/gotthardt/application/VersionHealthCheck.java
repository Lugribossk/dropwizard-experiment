package bo.gotthardt.application;

import com.codahale.metrics.health.HealthCheck;

/**
 * A health check that displays the Maven version of the running code.
 * Is unhealthy if it is not available or a SNAPSHOT version.
 *
 * @author Bo Gotthardt
 */
public class VersionHealthCheck extends HealthCheck {
    private final BuildInfo buildInfo;

    public VersionHealthCheck() {
        this.buildInfo = BuildInfo.create();
    }

    @Override
    protected Result check() throws Exception {
        if (!buildInfo.isValid()) {
            return Result.unhealthy("Running non-Maven build, no version info available.");
        } else if (buildInfo.getVersion().contains("SNAPSHOT")) {
            return Result.unhealthy("Running snapshot build: %s", buildInfo.getVersion());
        } else {
            return Result.healthy(buildInfo.getVersion());
        }
    }
}
