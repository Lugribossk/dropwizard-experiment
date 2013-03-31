package bo.gotthardt.frontend;

import com.yammer.dropwizard.Bundle;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import lombok.RequiredArgsConstructor;

/**
 * Bundle that enables this module to be added to a Dropwizard application as a whole.
 *
 * @author Bo Gotthardt
 */
@RequiredArgsConstructor
public class FrontendBundle implements Bundle {
    private final String baseUrl;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/bo/gotthardt/frontend/assets/", baseUrl, "index.html"));
    }

    @Override
    public void run(Environment environment) {
        // Empty on purpose.
    }
}
