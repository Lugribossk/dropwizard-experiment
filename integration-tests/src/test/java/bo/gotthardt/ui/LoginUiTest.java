package bo.gotthardt.ui;

import bo.gotthardt.model.User;
import bo.gotthardt.test.UiIntegrationTest;
import bo.gotthardt.ui.page.LoginPage;
import org.junit.Test;

/**
 * @author Bo Gotthardt
 */
public class LoginUiTest extends UiIntegrationTest {
    @Test
    public void shouldLogInWithCorrectCredentials() {
        db.save(new User("testuser", "testpassword"));
        LoginPage page = LoginPage.go(driver);

        page.loginWithCorrectCredentials("testuser", "testpassword");
    }

    @Test
    public void shouldStayLoggedOutWithWrongCredentials() {
        LoginPage page = LoginPage.go(driver);

        page.loginWithWrongCredentials("testuser", "WRONGPASSWORD");
    }
}
