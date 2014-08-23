package bo.gotthardt.application;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.dropwizard.jackson.Jackson;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Information about the build of the running code.
 * Requires Maven resource filtering and the git-commit-id-plugin plugin to be set up.
 *
 * @author Bo Gotthardt
 */
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BuildInfo {
    /** Maven version. */
    private String version;
    /** Maven build timestamp. */
    private DateTime builtAt;
    /* Git revision. */
    private String revision;
    /** Git branch name. */
    private String branch;

    /**
     * Whether the build information is valid.
     * This can be false if running a build not created by Maven or something integrated with Maven.
     */
    public boolean isValid() {
        return version != null && !version.contains("$");
    }

    public static BuildInfo create() {
        try {
            String info = Resources.toString(Resources.getResource("build.json"), Charsets.UTF_8);
            return Jackson.newObjectMapper().readValue(info, BuildInfo.class);
        } catch (IOException e) {
            log.warn("Unable to read and map build information.", e);
            return new BuildInfo();
        }
    }
}
