package bo.gotthardt.test;

import bo.gotthardt.todolist.application.TodoListApplication;
import bo.gotthardt.todolist.application.TodoListConfiguration;
import com.avaje.ebean.EbeanServer;
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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

/**
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

        driver = getDriver(caps);
        db = appRule.<TodoListApplication>getApplication().getEbeanBundle().getEbeanServer();
    }

    @AfterClass
    public static void teardownWebDriver() {
        driver.quit();
    }

    @After
    public void clearLocalStorage() {
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear()");
    }

    private static String getConfigFilePath() {
        String path = Resources.getResource("integration.yml").toString();

        if (path.startsWith("file://")) {
            return path.substring(6);
        } else {
            return path.substring(5);
        }
    }

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

    private static WebDriver getDriver(DesiredCapabilities caps) {
        String driver = System.getenv(WEBDRIVER_ENV_NAME);

        if (driver == null) {
            driver = "firefox";
        }

        switch (driver) {
            case "phantomjs":
                String binary = "node_modules/phantomjs/lib/phantom/" + (System.getProperty("os.name").contains("Windows") ? "phantomjs.exe" : "bin/phantomjs");
                List<String> locations = ImmutableList.of(binary,
                                                          "integration-tests/" + binary,
                                                          "/usr/local/phantomjs/bin/phantomjs"); // Default install location on Travis CI.

                for (String location : locations) {
                    File file = new File(location);
                    if (file.exists()) {
                        log.info("Using PhantomJS binary from {}", file.getAbsolutePath());

                        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, location);
                        return new PhantomJSDriver(caps);
                    } else {
                        log.info("PhantomJS binary not found at {}", file.getAbsolutePath());
                    }
                }
                throw new IllegalStateException("No PhantomJS binary found.");

            case "chrome":
                return new ChromeDriver(caps);

            default:
                log.warn("Unknown WebDriver type '{}', defaulting to Firefox.", driver);
            case "firefox":
                return new FirefoxDriver(caps);
        }
    }
}
