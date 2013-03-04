package bo.gotthardt.util;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import lombok.Delegate;

/**
 * @author Bo Gotthardt
 */
public class InMemoryEbeanServer implements EbeanServer {
    @Delegate(types=EbeanServer.class)
    private final EbeanServer server;

    public InMemoryEbeanServer() {
        // Load the in-memory database configuration and set it as the default server.
        ServerConfig config = new ServerConfig();
        config.setName("h2");
        config.loadFromProperties();
        // Generate and run database creation queries so we start with an empty db.
        config.setDdlGenerate(true);
        config.setDdlRun(true);
        // Set as default server so it can also be accessed though the Ebean singleton.
        config.setDefaultServer(true);
        server = EbeanServerFactory.create(config);
    }
}
