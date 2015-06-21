package bo.gotthardt.jersey;

import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.servlet.ServletContainer;

public class HK2Utils {
    public static ServiceLocator getServiceLocator(Environment environment) {
        return ((ServletContainer) environment.getJerseyServletContainer()).getApplicationHandler().getServiceLocator();
    }
}
