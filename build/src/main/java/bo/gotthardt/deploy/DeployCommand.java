package bo.gotthardt.deploy;

import bo.gotthardt.application.BuildToolConfiguration;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import jp.co.flect.heroku.platformapi.PlatformApi;
import jp.co.flect.heroku.platformapi.model.App;
import jp.co.flect.heroku.platformapi.model.Release;
import jp.co.flect.heroku.platformapi.model.Slug;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Dropwizard command for deploying to Heroku.
 *
 * Usage: deploy --application my-app-1234 --jar todo/todo-server/target/todo-server-0.0.1-SNAPSHOT.jar --configuration todo/todo-server/src/main/resources/configuration.yml build/src/main/resources/deploy.yml
 */
@Slf4j
public class DeployCommand extends ConfiguredCommand<BuildToolConfiguration> {
    private static final long SLUG_SIZE_LIMIT = 300 * 1024 * 1024;
    private static final long SLUG_SIZE_WARNING = Math.round(SLUG_SIZE_LIMIT * 0.80);

    private PlatformApi heroku;

    public DeployCommand() {
        super("deploy", "blah");
    }

    @Override
    protected void run(Bootstrap<BuildToolConfiguration> bootstrap, Namespace namespace, BuildToolConfiguration configuration) throws Exception {
        HerokuCredentials credentials = configuration.getHeroku();
        heroku = PlatformApi.fromApiKey(credentials.getUsername(), credentials.getApiKey());

        String appName = namespace.getString("application");
        validateAppName(appName);

        File jarFile = (File) namespace.get("jar");
        File configFile = (File) namespace.get("configuration");
        log.info("Found jar file: {}", jarFile.getAbsolutePath());
        log.info("Found configuration file: {}", configFile.getAbsolutePath());

        File slugArchive = createSlugArchive(jarFile, configFile);

        Map<String, String> processTypes = createProcessTypes(jarFile, configFile);

        Slug slug = uploadSlug(appName, processTypes, slugArchive);

        releaseSlug(appName, slug);
        log.info("Deployment complete!");
    }

    private App validateAppName(String appName) {
        log.info("Validating app name...");
        try {
            App app = heroku.getApp(appName);
            log.info("Found app: '{}' with id {}.", appName, app.getId());
            return app;
        } catch (IOException e) {
            log.error("Unable to find app.", e);
            quit();
            return null;
        }
    }

    private File createSlugArchive(File jarFile, File configFile) throws IOException {
        DeployCommand.log.info("Creating slug archive...");
        try {
            File javaConfig = new File(Resources.getResource("system.properties").toURI());
            File slugArchive = TarGzArchive.create(ImmutableSet.of(jarFile, configFile, javaConfig), "app");
            long size = slugArchive.length();
            log.info("Created slug archive with a size of {} bytes.", size);
            if (size > SLUG_SIZE_LIMIT) {
                log.warn("Slug size is over the Heroku limit of {} bytes.", SLUG_SIZE_LIMIT);
            } else if (size > SLUG_SIZE_WARNING) {
                log.warn("Slug size is approaching the Heroku limit of {} bytes.", SLUG_SIZE_LIMIT);
            }
            return slugArchive;
        } catch (IOException | URISyntaxException e) {
            log.error("Unable to create slug archive", e);
            quit();
            return null;
        }
    }

    private static Map<String, String> createProcessTypes(File jarFile, File configFile) {
        return ImmutableMap.of("web", "java -Ddw.server.connector.port=$PORT -jar " + jarFile.getName() + " server " + configFile.getName());
    }

    private Slug uploadSlug(String appName, Map<String, String> processTypes, File slugArchive) {
        log.info("Uploading slug to Heroku, this may take a few minutes...");
        try {
            Slug slug = heroku.createSlug(appName, processTypes, slugArchive, "TODO");
            log.info("Uploaded slug with id {}.", slug.getId());
            return slug;
        } catch (IOException e) {
            log.error("Unable to create slug.", e);
            slugArchive.delete();
            quit();
            return null;
        }
    }

    private Release releaseSlug(String appName, Slug slug) {
        log.info("Releasing slug...");
        try {
            Release release = heroku.createRelease(appName, slug.getId(), "DeployCommand");
            log.info("Slug released as version {}.", release.getVersion());
            return release;
        } catch (IOException e) {
            log.error("Unable to release slug to app.", e);
            quit();
            return null;
        }
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("--application").required(true);
        subparser.addArgument("--jar").type(Arguments.fileType().verifyCanRead()).required(true);
        subparser.addArgument("--configuration").type(Arguments.fileType().verifyCanRead()).required(true);
        super.configure(subparser);
    }

    private static void quit() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Blah
        }
        System.exit(-1);
    }
}
