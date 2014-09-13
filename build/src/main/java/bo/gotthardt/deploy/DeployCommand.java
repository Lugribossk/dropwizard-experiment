package bo.gotthardt.deploy;

import bo.gotthardt.application.BuildToolConfiguration;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import jp.co.flect.heroku.platformapi.PlatformApi;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Dropwizard command for deploying to Heroku.
 *
 * Usage: java -jar build-blah.jar deploy --application my-app-1234 --jar todo/todo-server/target/todo-server-0.0.1-SNAPSHOT.jar --configuration todo/todo-server/config/config.yml build/config/deploy.yml
 */
@Slf4j
public class DeployCommand extends ConfiguredCommand<BuildToolConfiguration> {
    private static final String JRE_URL = "http://download.oracle.com/otn-pub/java/jdk/8u20-b26/jre-8u20-linux-x64.tar.gz";

    public DeployCommand() {
        super("deploy", "Utility to deploy an application to Heroku.\n" +
                "(The file argument is the config file for this program, while the --configuration argument is the config file for the uploaded app.)");
    }

    @Override
    protected void run(Bootstrap<BuildToolConfiguration> bootstrap, Namespace namespace, BuildToolConfiguration configuration) throws Exception {
        HerokuCredentials credentials = configuration.getHeroku();

        String appName = namespace.getString("application");
        File jarFile = namespace.get("jar");
        File configFile = namespace.get("configuration");
        String jreUrl = namespace.get("jre");

        log.info("Using jar file: {}", jarFile.getAbsolutePath());
        log.info("Using configuration file: {}", configFile.getAbsolutePath());

        File jrePack = downloadJre(jreUrl);
        log.info("Using JRE: {}", jrePack.getAbsolutePath());

        File jreDir = TarGzArchive.unpack(jrePack);

        PlatformApi heroku = PlatformApi.fromApiKey(credentials.getUsername(), credentials.getApiKey());
        new HerokuDeployer(heroku).deploy(appName, jarFile, configFile, jreDir, "TODO"); // TODO revision

        Unirest.shutdown();
        log.info("Deployment complete!");
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("--application")
                .required(true)
                .help("Application name on Heroku.");

        subparser.addArgument("--jar")
                .type(Arguments.fileType().verifyCanRead())
                .required(true)
                .help("Path to jar file to deploy.");

        subparser.addArgument("--configuration")
                .type(Arguments.fileType().verifyCanRead())
                .required(true)
                .help("Path to configuration file to deploy.");

        subparser.addArgument("--jre")
                .setDefault(JRE_URL)
                .help("URL to download JRE from.");

        super.configure(subparser);
    }

    private static File downloadJre(String url) {
        File jrePack = new File("build/target/" + url.substring(url.lastIndexOf("/")));

        if (!jrePack.exists()) {
            log.info("Downloading JRE, this may take a moment...");
            try {
                HttpResponse<InputStream> request = Unirest.get(url)
                        .header("Cookie", "oraclelicense=accept-securebackup-cookie")
                        .asBinary();

                Files.copy(request.getBody(), jrePack.toPath());
            } catch (UnirestException | IOException e) {
                log.error("Unable to download JRE.", e);
            }
        }

        return jrePack;
    }
}
