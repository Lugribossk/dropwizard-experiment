package bo.gotthardt;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

/**
 * Abstract test that sets up an in-memory database so Ebean just works&trade;.
 * The database is cleared between tests.
 *
 * @see <a href="http://blog.matthieuguillermin.fr/2012/03/unit-testing-tricks-for-play-2-0-and-ebean/">Blog post</a>
 * @author Bo Gotthardt
 */
public abstract class EbeanInMemoryTest {
    private static DdlGenerator ddl;

    @BeforeClass
    public static void setupGenerator() {
        // Create a DdlGenerator for executing SQL.
        SpiEbeanServer server = (SpiEbeanServer) Ebean.getServer("h2");
        ddl = new DdlGenerator(server, new H2Platform(), new ServerConfig());

        try {
            // DdlGenerator spams System.out when it executes statements, so block that.
            Field out = DdlGenerator.class.getDeclaredField("out");
            PrintStream stream = new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    // Do nothing.
                }
            });

            out.setAccessible(true);
            out.set(ddl, stream);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unable to redirect DdlGenerator output.", e);
        }
    }

    @Before
    public void resetDatabase() {
        // Reset the database by dropping and creating it.
        ddl.runScript(false, ddl.generateDropDdl());
        ddl.runScript(false, ddl.generateCreateDdl());
    }
}
