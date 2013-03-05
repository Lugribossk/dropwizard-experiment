package bo.gotthardt.ebean;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.yammer.dropwizard.Bundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

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
        environment.addHealthCheck(new EbeanHealthCheck(getDefaultServer()));
    }

    public EbeanServer getDefaultServer() {
        return Ebean.getServer(null);
    }

    public EbeanServer getServer(String name) {
        return Ebean.getServer(name);
    }
}
