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

        page.loginSuccess("testuser", "testpassword");
    }

    @Test
    public void shouldStayLoggedOutWithNonexistentCredentials() {
        LoginPage page = LoginPage.go(driver);

        page.loginFail("testuser", "testpassword");
    }

    @Test
    public void shouldStayLoggedOutWithWrongPassword() {
        db.save(new User("testuser", "testpassword"));
        LoginPage page = LoginPage.go(driver);

        page.loginFail("testuser", "WRONGpassword");
    }
}
