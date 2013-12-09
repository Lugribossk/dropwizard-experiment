package bo.gotthardt.util;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import lombok.Delegate;

import javax.persistence.PersistenceException;

/**
 * In-memory EbeanServer for use in unit/integration tests.
 *
 * @author Bo Gotthardt
 */
public class InMemoryEbeanServer implements EbeanServer {
    private static final String SUBCLASSING_ERROR = "This configuration does not allow entity subclassing ";

    @Delegate(types=EbeanServer.class)
    private final EbeanServer server;

    public InMemoryEbeanServer() {
        // Load the in-memory database configuration.
        ServerConfig config = new ServerConfig();
        config.setName("h2");
        config.loadFromProperties();

        // We'd like to generate and run database creation queries before each test.
        // But we'd also like to not have System.out spammed by Ebean's DdlGenerator class that performs this.
        // If generation/run is enabled, it will do so as soon as we create the server, so turn it off, and do it manually later.
        config.setDdlGenerate(false);
        config.setDdlRun(false);

        // Turn off SQL logging as it is rather spammy.
        //config.setDebugSql(false);

        // Production uses enhancement, so turn off subclassing so we will get en error up front if a test run defaults to using it.
        //config.setAllowSubclassing(false);

        // Set as default server in case anyone is using the Ebean singleton.
        config.setDefaultServer(true);

        try {
            server = EbeanServerFactory.create(config);

        } catch (PersistenceException e) {
            // Ebean will throw this exception with a particular error message when subclassing is turned off but used anyway.
            String message = e.getMessage();
            if (message != null && message.contains(SUBCLASSING_ERROR)) {
                // Rethrow as a more informative error.
                throw new RuntimeException("A test involving Ebean has defaulted to using subclassing rather than enhancement.\n" +
                        "  Is your IDE set up to run unit tests with the Ebean javaagent?\n" +
                        "  Or is " + message.substring(SUBCLASSING_ERROR.length()) + " located in a package that the ebean-maven-enhancement plugin is not configured for?");
            } else {
                throw e;
            }
        }

        // Block DdlGenerator from spamming System.out by replacing its private "out" PrintStream with a dummy.
        DdlGenerator ddl = ((SpiEbeanServer) server).getDdlGenerator();

        /*PrintStream stream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing.
            }
        });
        Reflection.field("out").ofType(PrintStream.class).in(ddl).set(stream);*/

        // And finally run the database creation queries.
        ddl.runScript(false, ddl.generateDropDdl());
        ddl.runScript(false, ddl.generateCreateDdl());
    }
}
