package bo.gotthardt.ebean;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.CloseableLiquibase;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import liquibase.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Dropwizard bundle for setting up Ebean.
 * Also applies Liquibase database migrations when run.
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
        ExtendedDataSourceFactory dbConfig = configuration.getDatabaseConfig();
        log.info("Connecting to database on '{}' with username '{}'.", dbConfig.getUrl(), dbConfig.getUser());

        if (dbConfig.isMigrationsEnabled()) {
            try {
                applyMigrations(dbConfig, environment.metrics());
            } catch (Exception e) {
                log.error("Error applying database migrations! {}", e.getMessage());
                throw e;
            }
        } else {
            log.info("Database migrations disabled.");
        }

        ServerConfig serverConfig = EbeanConfigUtils.createServerConfig(dbConfig);
        ebeanServer = EbeanServerFactory.create(serverConfig);
        Preconditions.checkNotNull(ebeanServer);

        environment.healthChecks().register("ebean-" + ebeanServer.getName(), new EbeanHealthCheck(ebeanServer));
    }

    /**
     * Get the configured EbeanServer.
     * Only available after bundles have been initialized.
     */
    public EbeanServer getEbeanServer() {
        Preconditions.checkNotNull(ebeanServer, "Ebean server not created yet (this happens during 'run' i.e. after 'initialize').");
        return ebeanServer;
    }

    private static void applyMigrations(DataSourceFactory dbConfig, MetricRegistry metrics) throws Exception {
        Stopwatch migrationsTimer = Stopwatch.createStarted();

        // Borrowed from AbstractLiquibaseCommand.
        DataSourceFactory lbConfig = EbeanConfigUtils.clone(dbConfig);
        lbConfig.setMaxSize(1);
        lbConfig.setMinSize(1);
        lbConfig.setInitialSize(1);

        try (CloseableLiquibase liquibase = new CloseableLiquibase(dbConfig.build(metrics, "liquibase"))) {
            log.info("Checking for database migrations.");
            liquibase.update("");

            migrationsTimer.stop();
            metrics.timer(MetricRegistry.name(EbeanBundle.class, "migrations")).update(migrationsTimer.elapsed(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
            log.info("Database migrations complete in {} ms.", migrationsTimer.elapsed(TimeUnit.MILLISECONDS));
        } catch (ValidationFailedException e) {
            e.printDescriptiveError(System.err);
            throw e;
        }
    }
}
