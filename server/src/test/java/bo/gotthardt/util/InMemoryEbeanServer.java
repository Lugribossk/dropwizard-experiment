package bo.gotthardt.util;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import lombok.Delegate;
import org.fest.reflect.core.Reflection;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * In-memory EbeanServer for use in unit/integration tests.
 *
 * @author Bo Gotthardt
 */
public class InMemoryEbeanServer implements EbeanServer {
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
        config.setDebugSql(false);

        // Set as default server in case anyone is using the Ebean singleton.
        config.setDefaultServer(true);
        server = EbeanServerFactory.create(config);

        // Block DdlGenerator from spamming System.out by replacing its private "out" PrintStream with a dummy.
        DdlGenerator ddl = ((SpiEbeanServer) server).getDdlGenerator();

        PrintStream stream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing.
            }
        });
        Reflection.field("out").ofType(PrintStream.class).in(ddl).set(stream);

        // And finally run the database creation queries.
        ddl.runScript(false, ddl.generateDropDdl());
        ddl.runScript(false, ddl.generateCreateDdl());
    }
}
