package bo.gotthardt.application;

import bo.gotthardt.configuration.ApiConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Actual application that combines the frontend and API parts into a complete working whole.
 *
 * @author Bo Gotthardt
 */
public class TodoApplication extends Application<ApiConfiguration> {

    public static void main(String... args) throws Exception {
        new TodoApplication().run(args);
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
