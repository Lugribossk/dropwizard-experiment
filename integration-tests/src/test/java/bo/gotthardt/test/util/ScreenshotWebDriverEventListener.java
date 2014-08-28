package bo.gotthardt.test.util;

import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * Save screenshots of test WebDriver test failures via it's event listener option.
 *
 * @author Bo Gotthardt
 */
@Slf4j
@RequiredArgsConstructor
public class ScreenshotWebDriverEventListener extends AbstractWebDriverEventListener {
    private final String classPrefix;

    // TODO Investigate why this also captures screenshots of non-failing tests.

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        // Chrome, Firefox and PhantomJS can all take screenshots.
        if (driver instanceof TakesScreenshot) {
            byte[] imageBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            try {
                File screenshot = new File("integration-tests/target/screenshots/" + getFilename(throwable) + ".png");
                Files.write(imageBytes, screenshot);
                log.warn("Saved test failure screenshot to {}", screenshot.getAbsolutePath());
            } catch (IOException e) {
                log.error("Unable to save screenshot of test failure.", e);
            }
        }
    }

    /**
     * Get a human-readable filename that identifies the source fo the specified exception.
     *
     * @param throwable The exception
     * @return The filename
     */
    private String getFilename(Throwable throwable) {
        String failureSite = null;
        for (StackTraceElement element : throwable.getStackTrace()) {
            try {
                String className = element.getClassName();

                // Only consider our own classes.
                if (className.startsWith(classPrefix)) {
                    Class<?> klass = Class.forName(className);

                    // The first of our classes we find must be the actual site of the failure.
                    if (failureSite == null) {
                        // E.g. PageObject.waitFor#27
                        failureSite = klass.getSimpleName() + "." + element.getMethodName() + "#" + element.getLineNumber();
                    }

                    Method method = klass.getMethod(element.getMethodName());
                    for (Annotation annotation : method.getDeclaredAnnotations()) {
                        if (annotation instanceof Test) {
                            // Once we find one of our classes annotated with @Test it must be the case that fails.
                            // E.g. LoginUiTest.shouldBlahBlah-
                            String failingTest = klass.getSimpleName() + "." + method.getName();
                            return failingTest + "-" + failureSite;
                        }
                    }
                }
            } catch (ReflectiveOperationException e) {
                // Empty on purpose, I guess hasMethod() would have been to easy compared to throwing exceptions...
            }
        }
        return "Unknown" + new Random().nextInt(10000);
    }

    /**
     * Set up the specified WebDriver to take screenshots.
     *
     * @param driver The driver
     * @param classPrefix The package name prefix of your own classes (as opposed to Selenium, standard Java, etc.) that the test cases are located in.
     * @return The modified WebDriver
     */
    public static WebDriver applyTo(WebDriver driver, String classPrefix) {
        if (driver instanceof EventFiringWebDriver) {
            ((EventFiringWebDriver) driver).register(new ScreenshotWebDriverEventListener(classPrefix));
            return driver;
        } else {
            EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
            eventDriver.register(new ScreenshotWebDriverEventListener(classPrefix));
            return eventDriver;
        }
    }
}
