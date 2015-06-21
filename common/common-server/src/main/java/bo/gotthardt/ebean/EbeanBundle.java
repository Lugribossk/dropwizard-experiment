package bo.gotthardt.ebean;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Preconditions;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.CloseableLiquibase;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import liquibase.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Bo Gotthardt
 */
@Slf4j
public class EbeanBundle implements ConfiguredBundle<HasDatabaseConfiguration> {
    private EbeanServer ebeanServer;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // Empty on purpose.
    }

    @Override
    public void run(HasDatabaseConfiguration configuration, Environment environment) throws Exception {
        DataSourceFactory dbConfig = configuration.getDatabase();
        log.info("Connecting to database on '{}' with username '{}'.", dbConfig.getUrl(), dbConfig.getUser());

        try {
            applyMigrations(dbConfig, environment.metrics());
        } catch (Exception e) {
            log.error("Error applying database migrations! {}", e.getMessage());
            throw e;
        }

        ServerConfig serverConfig = EbeanUtils.createServerConfig(dbConfig);
        ebeanServer = EbeanServerFactory.create(serverConfig);
        Preconditions.checkNotNull(ebeanServer);

        environment.healthChecks().register("ebean-" + ebeanServer.getName(), new EbeanHealthCheck(ebeanServer));
    }

    public EbeanServer getEbeanServer() {
        Preconditions.checkNotNull(ebeanServer, "Ebean server not created yet (this happens during 'run' i.e. after 'initialize').");
        return ebeanServer;
    }

    private static void applyMigrations(DataSourceFactory dbConfig, MetricRegistry metrics) throws Exception {
        // Borrowed from AbstractLiquibaseCommand.
        int max = dbConfig.getMaxSize();
        int min = dbConfig.getMinSize();
        int initial = dbConfig.getInitialSize();
        dbConfig.setMaxSize(1);
        dbConfig.setMinSize(1);
        dbConfig.setInitialSize(1);
        try (CloseableLiquibase liquibase = new CloseableLiquibase(dbConfig.build(metrics, "liquibase"))) {
            log.info("Checking for database migrations.");
            liquibase.update("");
            log.info("Database migrations complete.");
        } catch (ValidationFailedException e) {
            e.printDescriptiveError(System.err);
            throw e;
        } finally {
            dbConfig.setMaxSize(max);
            dbConfig.setMinSize(min);
            dbConfig.setInitialSize(initial);
        }
    }
}
