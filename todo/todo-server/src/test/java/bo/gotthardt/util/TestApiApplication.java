package bo.gotthardt.util;

import bo.gotthardt.application.ApiBundle;
import bo.gotthardt.configuration.ApiConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author Bo Gotthardt
 */
public class TestApiApplication extends Application<ApiConfiguration> {

    public static void main(String... args) throws Exception {
        new TestApiApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApiConfiguration> bootstrap) {
        bootstrap.addBundle(new ApiBundle());
    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        // Empty on purpose.
    }
}