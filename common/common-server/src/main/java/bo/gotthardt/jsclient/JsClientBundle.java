package bo.gotthardt.jsclient;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.RequiredArgsConstructor;

/**
 * Dropwizard bundle for serving the Javascript client part of an application, and its configuration.
 */
@RequiredArgsConstructor
public abstract class JsClientBundle implements ConfiguredBundle<HasJsClientConfiguration> {
    /** Path to the resources folder that contains the Javascript code and index.html. */
    private final String resourcePath;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        bootstrap.addBundle(new AssetsBundle(resourcePath, "/", "index.html"));
    }

    @Override
    public void run(HasJsClientConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new JsClientConfigurationResource(configuration.getJsClient()));
    }
}
