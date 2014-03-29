package bo.gotthardt.ui;

import bo.gotthardt.model.User;
import bo.gotthardt.test.UiIntegrationTest;
import bo.gotthardt.ui.page.LoginPage;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class LoginUiTest extends UiIntegrationTest {
    @Test
    public void shouldLogInWithCorrectCredentials() {
        db.save(new User("testuser", "testpassword"));
        LoginPage page = new LoginPage(driver).get();

        page.loginWithCorrectCredentials("testuser", "testpassword");
    }

    @Test
    public void shouldStayLoggedOutWithWrongCredentials() {
        LoginPage page = new LoginPage(driver).get();

        page.loginWithWrongCredentials("testuser", "WRONGPASSWORD");
    }
}
