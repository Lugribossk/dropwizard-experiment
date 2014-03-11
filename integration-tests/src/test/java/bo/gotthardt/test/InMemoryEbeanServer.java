package bo.gotthardt.test;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import lombok.Delegate;

/**
 * In-memory EbeanServer for use in unit/integration tests.
 *
 * @author Bo Gotthardt
 */
public class InMemoryEbeanServer implements EbeanServer {
    @Delegate(types=EbeanServer.class)
    private final EbeanServer server;
    private final DdlGenerator ddl;

    public void clear() {
        ddl.runScript(false, ddl.generateDropDdl());
        ddl.runScript(false, ddl.generateCreateDdl());
    }

    public InMemoryEbeanServer() {
        // Create in-memory database configuration.
        DataSourceConfig dbConfig = new DataSourceConfig();
        dbConfig.setUsername("sa");
        dbConfig.setPassword("");
        dbConfig.setUrl("jdbc:h2:mem:tests;DB_CLOSE_DELAY=-1");
        dbConfig.setDriver("org.h2.Driver");

        ServerConfig config = new ServerConfig();
        config.setName("h2");
        config.setDataSourceConfig(dbConfig);
        config.setDefaultServer(true);

        // This seems to need to be explicitly set to null to activate the new classpath scanning feature in 3.3.1-RC2.
        config.setClasses(null);

        // Generate and run database creation queries before each test.
        config.setDdlGenerate(true);
        config.setDdlRun(true);

        server = EbeanServerFactory.create(config);
        ddl = ((SpiEbeanServer) server).getDdlGenerator();
    }
}
