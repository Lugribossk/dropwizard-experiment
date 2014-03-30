package bo.gotthardt.ui.page;

import bo.gotthardt.test.PageObject;
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

    public DashboardPage loginWithCorrectCredentials(String name, String pass) {
        username.sendKeys(name);
        password.sendKeys(pass);
        submitButton.click();

        return new DashboardPage(driver);
    }

    public LoginPage loginWithWrongCredentials(String name, String pass) {
        username.sendKeys(name);
        password.sendKeys(pass);
        submitButton.click();

        waitFor(wrongPasswordAlert, "Wrong password warning");

        return new LoginPage(driver);
    }

    @Override
    protected void load() {
        assertThat(username).isVisible();
        assertThat(password).isVisible();
    }

    public static LoginPage go(WebDriver driver) {
        driver.get(BASE_URL);
        return new LoginPage(driver);
    }
}
