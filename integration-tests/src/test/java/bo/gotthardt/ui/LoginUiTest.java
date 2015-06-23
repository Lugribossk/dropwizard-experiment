package bo.gotthardt.ui;

import bo.gotthardt.model.User;
import bo.gotthardt.test.UiIntegrationTest;
import bo.gotthardt.page.DashboardPage;
import bo.gotthardt.page.LoginPage;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class LoginUiTest extends UiIntegrationTest {
    private static final String PASSWORD = "testpassword";

    @Before
    public void blah() {
    }

    @Test
    public void shouldLogInWithCorrectCredentials() {
        LoginPage page = frontPage();

        page.loginSuccess(user.getUsername(), PASSWORD);
    }

    @Test
    public void shouldGoBackToLoginFormAfterLoggingOut() {
        LoginPage page = frontPage();

        page.loginSuccess(user.getUsername(), PASSWORD)
                .logout();
    }

    @Test
    public void shouldStayLoggedOutWithNonexistentCredentials() {
        LoginPage page = frontPage();

        page.loginFail("doesnotexist", PASSWORD);
    }

    @Test
    public void shouldStayLoggedOutWithWrongPassword() {
        LoginPage page = frontPage();

        page.loginFail(user.getUsername(), "WRONGpassword");
    }

    @Test
    public void shouldLogInWithSecondUserAfterLoggingOut() {
        User user2 = new User("testuser2-" + new Date(), "testpassword2", "Test2 Testsen2");
        user2.setEmail("example2@example.com");
        db.save(user2);
        LoginPage page = frontPage();

        DashboardPage dash = page.loginSuccess(user.getUsername(), PASSWORD)
                                .logout()
                                .loginSuccess("testuser2", "testpassword2");

        assertThat(dash.getUserFullName()).isEqualTo("Test2 Testsen2");
    }

    @Test
    public void shouldStayLoggedInAfterReloadingPage() {
        LoginPage page = frontPage();

        page.loginSuccess(user.getUsername(), PASSWORD);
        driver.get(driver.getCurrentUrl());

        DashboardPage.go(driver);
    }
}
