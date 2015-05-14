package bo.gotthardt.webdriver;

import bo.gotthardt.test.PageObject;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * Save screenshots and console logs from WebDriver test failures via it's event listener option.
 *
 * @author Bo Gotthardt
 */
@Slf4j
@RequiredArgsConstructor
public class ReportingWebDriverEventListener extends AbstractWebDriverEventListener {
    private final String classPrefix;

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        String fileName = getFilename(throwable, driver);

        saveScreenshot(fileName, driver);
        saveLogs(fileName, driver);

        log.info("Saved test screenshot and logs to {}", fileName);
    }

    private String getFilename(Throwable throwable, WebDriver driver) {
        String dirName = "target/reports/";

        if (!System.getProperty("user.dir").contains("integration-tests")) {
            dirName = "integration-tests/" + dirName;
        }

        if (!new File(dirName).exists()) {
            new File(dirName).mkdir();
        }
        return dirName + getFailureSummary(throwable, driver);
    }

    /**
     * Get a short human-readable string that identifies the source of the specified exception.
     *
     * @param throwable The exception
     * @return The description, usable as a filename
     */
    private String getFailureSummary(Throwable throwable, WebDriver driver) {
        String failureSite = null;

        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
        String browser = caps.getBrowserName() + "-" + caps.getVersion();

        for (StackTraceElement element : throwable.getStackTrace()) {
            try {
                String className = element.getClassName();

                // Only consider our own classes.
                if (className.startsWith(classPrefix)) {
                    Class<?> klass = Class.forName(className);
                    String location = klass.getSimpleName() + "." + element.getMethodName() + "#" + element.getLineNumber();

                    // The first of our classes we find must be the actual site of the failure.
                    // Also ignore PageObject as almost all failures will come from there, and we want to be more specific.
                    if (failureSite == null && klass != PageObject.class) {
                        failureSite = location;
                    }

                    Method method = klass.getMethod(element.getMethodName());
                    for (Annotation annotation : method.getDeclaredAnnotations()) {
                        if (annotation instanceof Test) {
                            // Once we find one of our classes annotated with @Test it must be the case that fails.
                            return browser + "-" + location + "-" + failureSite;
                        }
                    }
                }
            } catch (ReflectiveOperationException e) {
                // Do nothing and proceed to the next element.
            }
        }
        return browser + "-Unknown" + new Random().nextInt(10000);
    }

    private static void saveScreenshot(String filename, WebDriver driver) {
        if (driver instanceof TakesScreenshot) {
            byte[] imageBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            try {
                File screenshot = new File(filename + ".png");
                Files.write(imageBytes, screenshot);
            } catch (IOException e) {
                log.error("Unable to save screenshot of test failure.", e);
            }
        }
    }

    private static void saveLogs(String filename, WebDriver driver) {
        StringBuffer logs = new StringBuffer();
        for (LogEntry entry : driver.manage().logs().get(LogType.BROWSER)) {
            logs.append(new DateTime(entry.getTimestamp()))
                    .append(": ")
                    .append(entry.getMessage())
                    .append("\n");
        }
        try {
            Files.write(logs, new File(filename + ".txt"), Charsets.UTF_8);
        } catch (IOException e) {
            log.error("Unable to save logs from test failure.", e);
        }
    }

    /**
     * Set up the specified WebDriver to take screenshots and save console logs.
     *
     * @param driver The driver. Must have been set up with Capability to capture browser logs.
     * @param classPrefix The package name prefix of your own classes (as opposed to Selenium, standard Java, etc.) that the test cases are located in.
     * @return The modified WebDriver
     */
    public static WebDriver applyTo(WebDriver driver, String classPrefix) {
        if (driver instanceof EventFiringWebDriver) {
            ((EventFiringWebDriver) driver).register(new ReportingWebDriverEventListener(classPrefix));
            return driver;
        } else {
            EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
            eventDriver.register(new ReportingWebDriverEventListener(classPrefix));
            return eventDriver;
        }
    }
}
