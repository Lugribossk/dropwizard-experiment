package bo.gotthardt.webdriver;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;

/**
 * Utility for creating WebDriver instances.
 */
@Slf4j
public class WebDriverFactory {
    private static final String WEBDRIVER_ENV_NAME = "WEBDRIVER";

    /**
     * Create a WebDriver instance with useful settings based on the WEBDRIVER environment variable.
     * @return The WebDriver
     */
    public static WebDriver create() {
        DesiredCapabilities caps = new DesiredCapabilities();
        withSystemProxy(caps);
        withBrowserLogs(caps);

        return withReports(getDriver(caps, System.getenv(WEBDRIVER_ENV_NAME)));
    }

    /**
     * Use the system proxy (i.e. Charles) if one is running.
     * This does not seem to happen automatically, even though it does happen automatically when running the browser normally.
     *
     * @param caps The capabilities to modify
     */
    private static void withSystemProxy(DesiredCapabilities caps) {
        List<Proxy> proxies = ProxySelector.getDefault().select(URI.create("http://www.google.com"));
        java.net.Proxy proxy = proxies.get(0);

        if (proxy.type() == java.net.Proxy.Type.HTTP) {
            InetSocketAddress address = (InetSocketAddress) proxy.address();
            if ("127.0.0.1".equals(address.getHostString())) {
                log.info("Detected local proxy on port {}, using for UI integration tests.", address.getPort());
                caps.setCapability(CapabilityType.PROXY, new org.openqa.selenium.Proxy().setHttpProxy("localhost:" + address.getPort()));
            }
        }
    }

    private static void withBrowserLogs(DesiredCapabilities caps) {
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logs);
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
            log.info("WebDriver type not specified, defaulting to Chrome.");
            browser = "chrome";
        }

        switch (browser.toLowerCase()) {
            case "firefox":
                return new FirefoxDriver(caps);

            default:
                log.warn("Unknown WebDriver type '{}', defaulting to Chrome.", browser);
            case "chrome":
                String chromedriverBinary = WebDriverBinaryFinder.findChromeDriver();
                System.setProperty("webdriver.chrome.driver", chromedriverBinary);
                log.info("Using ChromeDriver binary from {}", chromedriverBinary);

                // Prevent annoying yellow warning bar from being displayed.
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("excludeSwitches", ImmutableList.of("ignore-certificate-errors"));
                caps.setCapability(ChromeOptions.CAPABILITY, options);

                return new ChromeDriver(caps);

        }
    }

    private static WebDriver withReports(WebDriver driver) {
        return ReportingWebDriverEventListener.applyTo(driver, "bo.gotthardt");
    }
}
