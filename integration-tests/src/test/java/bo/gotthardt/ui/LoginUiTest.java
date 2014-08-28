package bo.gotthardt.ui;

import bo.gotthardt.model.User;
import bo.gotthardt.test.UiIntegrationTest;
import bo.gotthardt.ui.page.DashboardPage;
import bo.gotthardt.ui.page.LoginPage;
import org.junit.Test;
import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class LoginUiTest extends UiIntegrationTest {

    public static final String USERNAME = "testuser";
    public static final String PASSWORD = "testpassword";
    public static final String NAME = "Test Testsen";

    @Test
    public void shouldLogInWithCorrectCredentials() {
        db.save(new User(USERNAME, PASSWORD, NAME));
        LoginPage page = LoginPage.go(driver);

        page.loginSuccess(USERNAME, PASSWORD);
    }

    @Test
    public void shouldGoBackToLoginFormAfterLoggingOut() {
        db.save(new User(USERNAME, PASSWORD, NAME));
        LoginPage page = LoginPage.go(driver);

        page.loginSuccess(USERNAME, PASSWORD).logout();
    }

    @Test
    public void shouldStayLoggedOutWithNonexistentCredentials() {
        LoginPage page = LoginPage.go(driver);

        page.loginFail(USERNAME, PASSWORD);
    }

    @Test
    public void shouldStayLoggedOutWithWrongPassword() {
        db.save(new User(USERNAME, PASSWORD, NAME));
        LoginPage page = LoginPage.go(driver);

        page.loginFail(USERNAME, "WRONGpassword");
    }

    @Test
    public void shouldLogInWithSecondUserAfterLoggingOut() {
        db.save(new User(USERNAME, PASSWORD, NAME));
        db.save(new User("testuser2", "testpassword2", "Test2 Testsen2"));
        LoginPage page = LoginPage.go(driver);

        DashboardPage dash = page.loginSuccess(USERNAME, PASSWORD)
                                .logout()
                                .loginSuccess("testuser2", "testpassword2");

        assertThat(dash.getUserFullName()).isEqualTo("Test2 Testsen2");
    }

    @Test
    public void shouldStayLoggedInAfterReloadingPage() {
        db.save(new User(USERNAME, PASSWORD, NAME));
        LoginPage page = LoginPage.go(driver);

        page.loginSuccess(USERNAME, PASSWORD);
        driver.get(driver.getCurrentUrl());

        DashboardPage.go(driver);
    }
}
