package bo.gotthardt.test;

import bo.gotthardt.ebean.EbeanConfigUtils;
import bo.gotthardt.ebean.ExtendedDataSourceFactory;
import bo.gotthardt.ebean.HasDatabaseConfiguration;
import bo.gotthardt.model.User;
import bo.gotthardt.todolist.application.TodoListApplication;
import bo.gotthardt.todolist.application.TodoListConfiguration;
import bo.gotthardt.page.DashboardPage;
import bo.gotthardt.page.LoginPage;
import bo.gotthardt.webdriver.WebDriverFactory;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.FileConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.validation.valuehandling.OptionalValidatedValueUnwrapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.util.Date;

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
    private static String baseUrl;

    protected static WebDriver driver;
    protected static EbeanServer db;

    protected User user;

    @BeforeClass
    public static void setupWebDriver() {
        TodoListConfiguration config = loadConfigFile(TodoListConfiguration.class, "todo/todo-server/config/integration.yml");

        int port = ((HttpConnectorFactory) ((SimpleServerFactory) config.getServerFactory()).getConnector()).getPort();
        baseUrl = "http://localhost:" + port;
        log.info("Connecting to server on {}", baseUrl);

        DataSourceFactory dbConfig = config.getDatabaseConfig();
        Preconditions.checkState(dbConfig.getUrl().contains("test"), "Unexpected database name.");
        db = EbeanServerFactory.create(EbeanConfigUtils.createServerConfig(dbConfig));

        driver = WebDriverFactory.create();
    }

    @AfterClass
    public static void teardownWebDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Before
    public void createDefaultUser() {
        user = new User("testuser-" + new Date(), "testpassword", "Test Testsen");
        user.setEmail("example@example.com");
        db.save(user);
    }

    @After
    public void clearLocalStorage() {
        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript("window.localStorage.clear()");
        }
    }

    protected DashboardPage login() {
        return LoginPage.go(driver).loginSuccess(user.getUsername(), "testpassword");
    }

    protected LoginPage frontPage() {
        return LoginPage.go(driver);
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    private static <T extends Configuration & HasDatabaseConfiguration> T loadConfigFile(Class<T> klass, String path) {
        ValidatorFactory validatorFactory = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .addValidatedValueHandler(new OptionalValidatedValueUnwrapper())
            .buildValidatorFactory();
        ConfigurationFactory<T> configFactory = new ConfigurationFactory<>(klass, validatorFactory.getValidator(), Jackson.newObjectMapper(), "dw");
        try {
            return configFactory.build(new SubstitutingSourceProvider(new FileConfigurationSourceProvider(), new EnvironmentVariableSubstitutor()), path);
        } catch (IOException | ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
