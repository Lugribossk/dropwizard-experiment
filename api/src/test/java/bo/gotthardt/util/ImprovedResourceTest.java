package bo.gotthardt.util;

import org.junit.Before;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bo Gotthardt
 */
public abstract class ImprovedResourceTest {


    @Before
    public void squelchSpammyLoggers() {
        Logger.getLogger("com.sun.jersey").setLevel(Level.WARNING);
    }

}
