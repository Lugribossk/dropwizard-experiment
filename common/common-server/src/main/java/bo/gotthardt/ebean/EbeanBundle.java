package bo.gotthardt.ebean;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.Getter;

/**
 * @author Bo Gotthardt
 */
public class EbeanBundle implements ConfiguredBundle<HasEbeanConfiguration> {
    private EbeanServer ebeanServer;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // Empty on purpose.
    }

    @Override
    public void run(HasEbeanConfiguration configuration, Environment environment) throws Exception {
        ServerConfig serverConfig = getServerConfig(configuration.getEbean());
        ebeanServer = EbeanServerFactory.create(serverConfig);
        Preconditions.checkNotNull(ebeanServer);

        environment.healthChecks().register("ebean-" + ebeanServer.getName(), new EbeanHealthCheck(ebeanServer));
    }

    private static ServerConfig getServerConfig(EbeanConfiguration ebeanConfiguration) {
        ServerConfig config = new ServerConfig();
        config.setName("main");
        config.setDataSourceConfig(ebeanConfiguration.toDataSourceConfig());
        config.setDefaultServer(true);
        config.setPackages(ImmutableList.of("bo.gotthardt.model"));

        // Automatically create db tables on startup. TODO remove this when using a proper database.
        config.setDdlGenerate(true);
        config.setDdlRun(true);

        return config;
    }

    public EbeanServer getEbeanServer() {
        Preconditions.checkNotNull(ebeanServer, "Ebean server not created yet (this happens during 'run' i.e. after 'initialize').");
        return ebeanServer;
    }
}
