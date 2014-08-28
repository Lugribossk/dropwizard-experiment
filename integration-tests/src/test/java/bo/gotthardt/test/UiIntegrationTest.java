package bo.gotthardt.test;

import bo.gotthardt.test.util.WebDriverBinaryFinder;
import bo.gotthardt.todolist.application.TodoListApplication;
import bo.gotthardt.todolist.application.TodoListConfiguration;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import io.dropwizard.testing.junit.DropwizardAppRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

/**
 * Base class for UI integration tests that run via Selenium.
 * Access to the running app's database is available via the db property.
 *
 * Select which browser to use by setting the WEBDRIVER environment variable to "firefox", "chrome" or "phantomjs" (default).
 *
 * @author Bo Gotthardt
 */
@Slf4j
public abstract class UiIntegrationTest {
    private static final String WEBDRIVER_ENV_NAME = "WEBDRIVER";
    protected static WebDriver driver;
    protected static EbeanServer db;

    @ClassRule
    public static DropwizardAppRule<TodoListConfiguration> appRule = new DropwizardAppRule<>(TodoListApplication.class, getConfigFilePath());

    @BeforeClass
    public static void setupWebDriver() {
        DesiredCapabilities caps = new DesiredCapabilities();
        useSystemProxy(caps);

        driver = getDriver(caps, System.getenv(WEBDRIVER_ENV_NAME));
        db = appRule.<TodoListApplication>getApplication().getEbeanBundle().getEbeanServer();
    }

    @AfterClass
    public static void teardownWebDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @After
    public void clearLocalStorageAndDatabase() {
        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript("window.localStorage.clear()");
        }

        // TODO Do this with a transaction rollback instead?
        SpiEbeanServer realDb = (SpiEbeanServer) db;
        String driverClass = realDb.getServerConfig().getDataSourceConfig().getDriver();
        if (driverClass.equals("org.h2.Driver")) {
            DdlGenerator ddl = realDb.getDdlGenerator();
            ddl.runScript(false, ddl.generateDropDdl());
            ddl.runScript(false, ddl.generateCreateDdl());
        } else {
            log.error("Integration test does not appear to be using driver for in-memory testing, but rather {}. Not clearing database after test run.", driverClass);
        }
    }

    private static String getConfigFilePath() {
        String path = Resources.getResource("integration.yml").toString();

        if (path.startsWith("file://")) {
            return path.substring(6);
        } else {
            return path.substring(5);
        }
    }

    /**
     * Use the system proxy (i.e. Charles) if one is running.
     * This does not seem to happen automatically, even though it does happen automatically when running the browser normally.
     *
     * @param caps The capabilities to modify
     */
    private static void useSystemProxy(DesiredCapabilities caps) {
        List<java.net.Proxy> proxies = ProxySelector.getDefault().select(URI.create("http://www.google.com"));
        java.net.Proxy proxy = proxies.get(0);

        if (proxy.type() == java.net.Proxy.Type.HTTP) {
            InetSocketAddress address = (InetSocketAddress) proxy.address();
            if (address.getHostString().equals("127.0.0.1")) {
                log.info("Detected local proxy on port {}, using for UI integration tests.", address.getPort());
                caps.setCapability(CapabilityType.PROXY, new Proxy().setHttpProxy("localhost:" + address.getPort()));
            }
        }
    }

    /**
     * Get the WebDriver for the selected browser.
     *
     * @param caps The capabilities to use.
     * @param browser The browser to create a WebDriver client for.
     * @return The WebDriver
     */
    private static WebDriver getDriver(DesiredCapabilities caps, @Nullable String browser) {
        if (browser == null) {
            log.info("WebDriver type not specified, defaulting to PhantomJS.");
            browser = "phantomjs";
        }

        switch (browser.toLowerCase()) {
            case "firefox":
                return new FirefoxDriver(caps);

            case "chrome":
                String chromedriverBinary = WebDriverBinaryFinder.findChromeDriver();
                System.setProperty("webdriver.chrome.driver", chromedriverBinary);
                log.info("Using ChromeDriver binary from {}", chromedriverBinary);

                // Prevent annoying yellow warning bar from being displayed.
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("excludeSwitches", ImmutableList.of("ignore-certificate-errors"));
                caps.setCapability(ChromeOptions.CAPABILITY, options);

                return new ChromeDriver(caps);

            default:
                log.warn("Unknown WebDriver type '{}', defaulting to PhantomJS.", browser);
            case "phantomjs":
                String phantomBinary = WebDriverBinaryFinder.findPhantomJs();
                caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomBinary);
                log.info("Using PhantomJS binary from {}", phantomBinary);

                return new PhantomJSDriver(caps);
        }
    }
}
