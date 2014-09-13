package bo.gotthardt.application;

import bo.gotthardt.check.HealthCheckCommand;
import bo.gotthardt.deploy.DeployCommand;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author Bo Gotthardt
 */
public class BuildToolApplication extends Application<BuildToolConfiguration> {
    public static void main(String... args) throws Exception {
        new BuildToolApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<BuildToolConfiguration> bootstrap) {
        bootstrap.addCommand(new DeployCommand());
        bootstrap.addCommand(new HealthCheckCommand());
    }

    @Override
    public void run(BuildToolConfiguration configuration, Environment environment) throws Exception {
        // Empty on purpose.
    }
}
