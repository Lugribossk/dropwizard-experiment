package bo.gotthardt.test;

import bo.gotthardt.todolist.application.TodoListApplication;
import bo.gotthardt.todolist.application.TodoListConfiguration;
import com.avaje.ebean.EbeanServer;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author Bo Gotthardt
 */
public abstract class UiIntegrationTest {
    protected static WebDriver driver;
    protected static EbeanServer db;

    @ClassRule
    public static DropwizardAppRule<TodoListConfiguration> appRule = new DropwizardAppRule<>(TodoListApplication.class, "integration-tests/src/test/resources/integration.yml");

    @BeforeClass
    public static void setupWebDriver() {
        driver = new FirefoxDriver();
        db = appRule.<TodoListApplication>getApplication().getEbeanBundle().getEbeanServer();
    }

    @AfterClass
    public static void teardownWebDriver() {
        driver.quit();
    }
}
