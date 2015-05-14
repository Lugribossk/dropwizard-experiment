package bo.gotthardt.test;

import bo.gotthardt.model.User;
import bo.gotthardt.todolist.application.TodoListApplication;
import bo.gotthardt.todolist.application.TodoListConfiguration;
import bo.gotthardt.page.DashboardPage;
import bo.gotthardt.page.LoginPage;
import bo.gotthardt.webdriver.WebDriverFactory;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.google.common.io.Resources;
import io.dropwizard.testing.junit.DropwizardAppRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Base class for UI end-to-end tests that run via Selenium.
 * Access to the running app's database is available via the db property.
 *
 * Select which browser to use by setting the WEBDRIVER environment variable to "firefox" or "chrome" (default).
 *
 * @author Bo Gotthardt
 */
@Slf4j
public abstract class UiIntegrationTest {
    // Set in integration.yml
    public static String BASE_URL = "http://localhost:8056/";

    protected static WebDriver driver;
    protected static EbeanServer db;

    protected User user;

    @ClassRule
    public static DropwizardAppRule<TodoListConfiguration> appRule = new DropwizardAppRule<>(TodoListApplication.class, getConfigFilePath());

    @BeforeClass
    public static void setupWebDriver() {
        driver = WebDriverFactory.create();
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

        SpiEbeanServer realDb = (SpiEbeanServer) db;
        String driverClass = realDb.getServerConfig().getDataSourceConfig().getDriver();
        if ("org.h2.Driver".equals(driverClass)) {
            DdlGenerator ddl = realDb.getDdlGenerator();
            ddl.runScript(false, ddl.generateDropDdl());
            ddl.runScript(false, ddl.generateCreateDdl());
        } else {
            log.error("Integration test does not appear to be using driver for in-memory testing, but rather {}. Not clearing database after test run.", driverClass);
        }
    }

    protected DashboardPage login() {
        user = new User("testuser", "testpassword", "Test Testsen");
        db.save(user);
        return LoginPage.go(driver).loginSuccess("testuser", "testpassword");
    }

    protected LoginPage frontPage() {
        return LoginPage.go(driver);
    }

    private static String getConfigFilePath() {
        String path = Resources.getResource("integration.yml").toString();

        if (path.startsWith("file://")) {
            return path.substring(6);
        } else {
            return path.substring(5);
        }
    }
}
