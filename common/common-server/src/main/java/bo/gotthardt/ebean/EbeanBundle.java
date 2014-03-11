package bo.gotthardt.ebean;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.google.common.collect.ImmutableList;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.Getter;

/**
 * @author Bo Gotthardt
 */
public class EbeanBundle implements ConfiguredBundle<HasEbeanConfiguration> {
    @Getter
    private EbeanServer ebeanServer;

    @Override
    public void run(HasEbeanConfiguration configuration, Environment environment) throws Exception {
        ServerConfig serverConfig = getServerConfig(configuration.getEbean());
        ebeanServer = EbeanServerFactory.create(serverConfig);

        environment.healthChecks().register("ebean-" + ebeanServer.getName(), new EbeanHealthCheck(ebeanServer));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // Empty on purpose.
    }

    private static ServerConfig getServerConfig(EbeanConfiguration ebeanConfiguration) {
        ServerConfig config = new ServerConfig();
        config.setName("main");
        config.setDataSourceConfig(ebeanConfiguration.toDataSourceConfig());
        config.setDefaultServer(true);
        // TODO Do we need config.setClasses(null); here as well?
        config.setPackages(ImmutableList.of("bo.gotthardt.model.*"));

        return config;
    }
}
