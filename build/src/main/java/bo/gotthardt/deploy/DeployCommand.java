package bo.gotthardt.deploy;

import bo.gotthardt.application.BuildToolConfiguration;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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
import java.util.Map;

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
        DeployCommand.log.info("Found jar file: {}", jarFile.getAbsolutePath());
        DeployCommand.log.info("Found configuration file: {}", configFile.getAbsolutePath());

        File slugArchive = createSlugArchive(jarFile, configFile);

        Map<String, String> processTypes = createProcessTypes(jarFile, configFile);

        Slug slug = uploadSlug(appName, processTypes, slugArchive);

        releaseSlug(appName, slug);
    }

    private App validateAppName(String appName) {
        DeployCommand.log.info("Validating app name...");
        try {
            App app = heroku.getApp(appName);
            DeployCommand.log.info("Found app: '{}' with id {}.", appName, app.getId());
            return app;
        } catch (IOException e) {
            DeployCommand.log.error("Unable to find app.", e);
            System.exit(-1);
            return null;
        }
    }

    private Slug uploadSlug(String appName, Map<String, String> processTypes, File slugArchive) {
        DeployCommand.log.info("Uploading slug to Heroku...");
        try {
            Slug slug;
            slug = heroku.createSlug(appName, processTypes, slugArchive);
            DeployCommand.log.info("Uploaded slug with id {}.", slug.getId());
            return slug;
        } catch (IOException e) {
            DeployCommand.log.error("Unable to create slug.", e);
            System.exit(-1);
            return null;
        }
    }

    private Release releaseSlug(String appName, Slug slug) {
        DeployCommand.log.info("Releasing slug...");
        try {
            Release release = heroku.createRelease(appName, slug.getId(), "DeployCommand");
            DeployCommand.log.info("Released as v{}.", release.getVersion());
            return release;
        } catch (IOException e) {
            DeployCommand.log.error("Unable to release slug to app.", e);
            System.exit(-1);
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

    private static Map<String, String> createProcessTypes(File jarFile, File configFile) {
        return ImmutableMap.of("web", "java -jar " + jarFile.getName() + " server " + configFile.getName());
    }

    private static File createSlugArchive(File jarFile, File configFile) throws IOException {
        DeployCommand.log.info("Creating slug archive...");
        try {
            File slugArchive = TarGzArchive.create(ImmutableSet.of(jarFile, configFile), "app");
            long size = slugArchive.length();
            DeployCommand.log.info("Created slug archive with a size of {} bytes.", size);
            if (size > SLUG_SIZE_LIMIT) {
                DeployCommand.log.warn("Slug size is over the Heroku limit of {} bytes.", SLUG_SIZE_LIMIT);
            } else if (size > SLUG_SIZE_WARNING) {
                DeployCommand.log.warn("Slug size is approaching the Heroku limit of {} bytes.", SLUG_SIZE_LIMIT);
            }
            return slugArchive;
        } catch (IOException e) {
            DeployCommand.log.error("Unable to create slug archive", e);
            System.exit(-1);
            return null;
        }
    }
}
