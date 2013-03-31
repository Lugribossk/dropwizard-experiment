package bo.gotthardt.application;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.yammer.metrics.core.HealthCheck;

import java.io.IOException;

/**
 * A health check that displays the Maven version of the running code.
 * Is unhealthy if it is not available or a SNAPSHOT version.
 *
 * @author Bo Gotthardt
 */
public class VersionHealthCheck extends HealthCheck {
    private final String version;

    public VersionHealthCheck() throws IOException {
        super("version");
        this.version = Resources.toString(Resources.getResource("version.txt"), Charsets.UTF_8);
    }

    @Override
    protected Result check() throws Exception {
        if (version.contains("$")) {
            return Result.unhealthy("Running non-Maven build, no version info available.");
        } else if (version.contains("SNAPSHOT")) {
            return Result.unhealthy("Running snapshot build: %s", version);
        } else {
            return Result.healthy(version);
        }
    }
}
