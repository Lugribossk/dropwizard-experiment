package bo.gotthardt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.OffsetDateTime;

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
    private OffsetDateTime builtAt;
    /* Git revision. */
    private String revision;
    /** Git branch name. */
    private String branch;

    /**
     * Whether the build information is valid.
     * This can be false if running a build not created by Maven or something integrated with Maven.
     */
    public boolean isValid() {
        return version != null && !revision.contains("$");
    }

    public String getPrintableInfo() {
        String output = "Built at " + builtAt;
        if (isValid()) {
            output = output + ", revision: " + revision + ", branch: " + branch;
        }
        return output;
    }

    public static BuildInfo create(ObjectMapper mapper) {
        try {
            String info = Resources.toString(Resources.getResource("bo/gotthardt/application/build.json"), Charsets.UTF_8);
            return mapper.readValue(info, BuildInfo.class);
        } catch (IOException e) {
            log.warn("Unable to read and map build information.", e);
            return new BuildInfo();
        }
    }
}
