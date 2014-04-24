package bo.gotthardt.test;

import bo.gotthardt.todolist.application.TodoListApplication;
import bo.gotthardt.todolist.application.TodoListConfiguration;
import com.avaje.ebean.EbeanServer;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Bo Gotthardt
 */
public abstract class UiIntegrationTest {
    protected static WebDriver driver;
    protected static EbeanServer db;

    @ClassRule
    public static DropwizardAppRule<TodoListConfiguration> appRule = new DropwizardAppRule<>(TodoListApplication.class, "src/test/resources/integration.yml");

    @BeforeClass
    public static void setupWebDriver() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.PROXY, new Proxy().setHttpProxy("localhost:8888"));

        driver = new FirefoxDriver(caps);
        db = appRule.<TodoListApplication>getApplication().getEbeanBundle().getEbeanServer();
    }

    @AfterClass
    public static void teardownWebDriver() {
        driver.quit();
    }
}
