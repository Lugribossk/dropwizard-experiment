package bo.gotthardt.ui;

import bo.gotthardt.model.User;
import bo.gotthardt.test.UiIntegrationTest;
import bo.gotthardt.page.DashboardPage;
import bo.gotthardt.page.LoginPage;
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
        createUser();
        LoginPage page = frontPage();

        page.loginSuccess(USERNAME, PASSWORD);
    }

    @Test
    public void shouldGoBackToLoginFormAfterLoggingOut() {
        createUser();
        LoginPage page = frontPage();

        page.loginSuccess(USERNAME, PASSWORD)
                .logout();
    }

    @Test
    public void shouldStayLoggedOutWithNonexistentCredentials() {
        LoginPage page = frontPage();

        page.loginFail(USERNAME, PASSWORD);
    }

    @Test
    public void shouldStayLoggedOutWithWrongPassword() {
        createUser();
        LoginPage page = frontPage();

        page.loginFail(USERNAME, "WRONGpassword");
    }

    @Test
    public void shouldLogInWithSecondUserAfterLoggingOut() {
        createUser();
        User user2 = new User("testuser2", "testpassword2", "Test2 Testsen2");
        user2.setEmail("example2@example.com");
        db.save(user2);
        LoginPage page = frontPage();

        DashboardPage dash = page.loginSuccess(USERNAME, PASSWORD)
                                .logout()
                                .loginSuccess("testuser2", "testpassword2");

        assertThat(dash.getUserFullName()).isEqualTo("Test2 Testsen2");
    }

    @Test
    public void shouldStayLoggedInAfterReloadingPage() {
        createUser();
        LoginPage page = frontPage();

        page.loginSuccess(USERNAME, PASSWORD);
        driver.get(driver.getCurrentUrl());

        DashboardPage.go(driver);
    }

    private User createUser() {
        User user = new User(USERNAME, PASSWORD, NAME);
        user.setEmail("example@example.com");
        db.save(user);

        return user;
    }
}
