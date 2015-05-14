package bo.gotthardt.webdriver;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

/**
 * Utility for finding paths to the helper binaries needed by some WebDriver types.
 *
 * @author Bo Gotthardt
 */
public class WebDriverBinaryFinder {
    /**
     * Find the absolute path to the PhantomJS binary.
     * @return The path
     */
    public static String findPhantomJs() {
        // Assumptions:
        // 1. The current working directory is either the project root or the integration-tests module root.
        // 2. Npm has attempted to install phantomjs in integration-tests/node_modules (it is not actually installed if it already exists on the system).
        // 3. Regardless of install outcome, the path to the binary is placed in integration-tests/node_modules/phantomjs/lib.location.js .

        String locationJsPath = "node_modules/phantomjs/lib/location.js";
        boolean projectRoot = false;
        File locationJs = new File(locationJsPath);
        if (!locationJs.exists()) {
            projectRoot = true;
            locationJs = new File("integration-tests/" + locationJsPath);
        }
        Preconditions.checkState(locationJs.exists(), "Unable to find location.js. Check the working directory and whether PhantomJS has been installed.");

        try {
            String locationJsContent = Files.toString(locationJs, Charsets.UTF_8);
            String path = locationJsContent.substring(locationJsContent.indexOf("\"") + 1, locationJsContent.length() - 1);

            if (!path.startsWith("/")) {
                // The path is relative to the lib folder.
                path = (projectRoot ? "integration-tests/" : "") + "node_modules/phantomjs/lib/" + path.replace("\\\\", "/");
            }

            File binary = new File(path);
            Preconditions.checkState(binary.exists(), "Unable to find PhantomJS binary at %s from location.js.", binary.getAbsolutePath());
            return binary.getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Find the absolute path to the ChromeDriver binary.
     * @return The path
     */
    public static String findChromeDriver() {
        boolean isWindows = System.getProperty("os.name").contains("Windows");
        String path = "node_modules/chromedriver/lib/chromedriver/chromedriver" + (isWindows ? ".exe" : "");
        if (!new File(path).exists()) {
            path = "../" + path;
        }

        File binary = new File(path);
        Preconditions.checkState(binary.exists(), "Unable to find ChromeDriver binary. Check the working directory and whether ChromeDriver has been installed.");
        return binary.getAbsolutePath();
    }
}
