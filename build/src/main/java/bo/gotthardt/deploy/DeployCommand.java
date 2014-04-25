package bo.gotthardt.deploy;

import bo.gotthardt.application.BuildToolConfiguration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import jp.co.flect.heroku.platformapi.PlatformApi;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.io.File;

/**
 * Dropwizard command for deploying to Heroku.
 *
 * Usage: java -jar build-blah.jar deploy --application my-app-1234 --jar todo/todo-server/target/todo-server-0.0.1-SNAPSHOT.jar --configuration todo/todo-server/src/main/resources/heroku.yml build/src/main/resources/deploy.yml
 */
@Slf4j
public class DeployCommand extends ConfiguredCommand<BuildToolConfiguration> {
    public DeployCommand() {
        super("deploy", "blah");
    }

    @Override
    protected void run(Bootstrap<BuildToolConfiguration> bootstrap, Namespace namespace, BuildToolConfiguration configuration) throws Exception {
        HerokuCredentials credentials = configuration.getHeroku();

        String appName = namespace.getString("application");
        File jarFile = namespace.get("jar");
        File configFile = namespace.get("configuration");

        log.info("Found jar file: {}", jarFile.getAbsolutePath());
        log.info("Found configuration file: {}", configFile.getAbsolutePath());

        PlatformApi heroku = PlatformApi.fromApiKey(credentials.getUsername(), credentials.getApiKey());
        new HerokuDeployer(heroku).deploy(appName, jarFile, configFile, "TODO"); // TODO revision

        log.info("Deployment complete!");
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("--application").required(true);
        subparser.addArgument("--jar").type(Arguments.fileType().verifyCanRead()).required(true);
        subparser.addArgument("--configuration").type(Arguments.fileType().verifyCanRead()).required(true);
        super.configure(subparser);
    }
}
