package bo.gotthardt.deploy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import jp.co.flect.heroku.platformapi.PlatformApi;
import jp.co.flect.heroku.platformapi.model.App;
import jp.co.flect.heroku.platformapi.model.Release;
import jp.co.flect.heroku.platformapi.model.Slug;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Deploy a jar file to a Heroku app.
 *
 * See https://blog.heroku.com/archives/2013/12/20/programmatically_release_code_to_heroku_using_the_platform_api and
 * https://devcenter.heroku.com/articles/platform-api-deploying-slugs
 */
@Slf4j
@RequiredArgsConstructor
public class HerokuDeployer {
    private static final long SLUG_SIZE_LIMIT = 300 * 1024 * 1024;
    private static final long SLUG_SIZE_WARNING = Math.round(SLUG_SIZE_LIMIT * 0.80);
    private static final String JAVA_VERSION = "jre1.8.0_05";

    private final PlatformApi herokuApi;

    public Release deploy(String appName, File jarFile, File configFile, String revision) {
        Preconditions.checkState(jarFile.exists(), "Jar file not found.");
        Preconditions.checkState(configFile.exists(), "Configuration file not found.");

        validateAppName(appName);

        File slugArchive = createSlugArchive(jarFile, configFile);
        Map<String, String> processTypes = createProcessTypes(jarFile, configFile);

        Slug slug = uploadSlug(appName, processTypes, slugArchive, revision);
        return releaseSlug(appName, slug);
    }

    private App validateAppName(String appName) {
        log.info("Validating app name...");
        try {
            App app = herokuApi.getApp(appName);

            log.info("Found app: '{}' with id {}.", appName, app.getId());
            return app;
        } catch (IOException e) {
            log.error("Unable to find app.", e);
            throw new RuntimeException(e);
        }
    }

    private File createSlugArchive(File jarFile, File configFile) {
        log.info("Creating slug archive...");
        try {
            File jre = new File(Resources.getResource(JAVA_VERSION).toURI());
            File slugArchive = TarGzArchive.create(ImmutableSet.of(jarFile, configFile, jre), "app");
            slugArchive.deleteOnExit();

            long size = slugArchive.length();
            log.info("Created slug archive with a size of {} bytes.", size);
            if (size > SLUG_SIZE_LIMIT) {
                log.warn("Slug size is over the Heroku limit of {} bytes.", SLUG_SIZE_LIMIT);
            } else if (size > SLUG_SIZE_WARNING) {
                log.warn("Slug size is approaching the Heroku limit of {} bytes.", SLUG_SIZE_LIMIT);
            }
            return slugArchive;
        } catch (IOException | URISyntaxException e) {
            log.error("Unable to create slug archive.", e);
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> createProcessTypes(File jarFile, File configFile) {
        return ImmutableMap.of("web", JAVA_VERSION + "/bin/java -Ddw.server.connector.port=$PORT -jar " + jarFile.getName() + " server " + configFile.getName());
    }

    private Slug uploadSlug(String appName, Map<String, String> processTypes, File slugArchive, String revision) {
        log.info("Uploading slug to Heroku, this may take a few minutes...");
        try {
            Slug slug = herokuApi.createSlug(appName, processTypes, slugArchive, revision);

            log.info("Uploaded slug with id {}.", slug.getId());
            return slug;
        } catch (IOException e) {
            log.error("Unable to create slug.", e);
            throw new RuntimeException(e);
        }
    }

    private Release releaseSlug(String appName, Slug slug) {
        log.info("Releasing slug...");
        try {
            Release release = herokuApi.createRelease(appName, slug.getId(), "HerokuDeployer"); // TODO description

            log.info("Slug released as version {}.", release.getVersion());
            return release;
        } catch (IOException e) {
            log.error("Unable to release slug to app.", e);
            throw new RuntimeException(e);
        }
    }
}
