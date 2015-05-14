package bo.gotthardt.page;

import bo.gotthardt.test.PageObject;
import bo.gotthardt.test.UiIntegrationTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

public class LoginPage extends PageObject {
    @FindBy(id = "username")
    private WebElement username;
    @FindBy(id = "password")
    private WebElement password;
    @FindBy(className ="btn-primary")
    private WebElement submitButton;
    @FindBy(className = "alert")
    private WebElement wrongPasswordAlert;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public DashboardPage loginSuccess(String name, String pass) {
        username.sendKeys(name);
        password.sendKeys(pass);
        submitButton.click();

        return new DashboardPage(driver);
    }

    public LoginPage loginFail(String name, String pass) {
        username.sendKeys(name);
        password.sendKeys(pass);
        submitButton.click();

        waitFor(wrongPasswordAlert, "Wrong password warning");

        return new LoginPage(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(username, "Username input");
        assertThat(username).isVisible();
        assertThat(password).isVisible();
    }

    public static LoginPage go(WebDriver driver) {
        driver.get(UiIntegrationTest.BASE_URL);
        return new LoginPage(driver);
    }
}
