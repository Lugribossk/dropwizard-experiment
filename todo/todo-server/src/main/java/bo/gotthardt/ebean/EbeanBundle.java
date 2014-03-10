package bo.gotthardt.ebean;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author Bo Gotthardt
 */
public class EbeanBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // Ebean automatically loads its configuration, so nothing to do here.
        // TODO Consider using dropwizard config instead?
    }

    @Override
    public void run(Environment environment) {
        environment.healthChecks().register("ebean-" + getDefaultServer().getName(), new EbeanHealthCheck(getDefaultServer()));
    }

    public EbeanServer getDefaultServer() {
        return Ebean.getServer(null);
    }

    public EbeanServer getServer(String name) {
        return Ebean.getServer(name);
    }
}
