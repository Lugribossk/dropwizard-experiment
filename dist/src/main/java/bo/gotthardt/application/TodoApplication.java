package bo.gotthardt.application;

import bo.gotthardt.configuration.ApiConfiguration;
import bo.gotthardt.frontend.FrontendBundle;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * Actual application that combines the frontend and API parts into a complete working whole.
 *
 * @author Bo Gotthardt
 */
public class TodoApplication extends Service<ApiConfiguration> {

    public static void main(String... args) throws Exception {
        new TodoApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApiConfiguration> bootstrap) {
        bootstrap.addBundle(new ApiBundle());
        bootstrap.addBundle(new FrontendBundle("/"));
    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        // Empty on purpose.
    }
}
