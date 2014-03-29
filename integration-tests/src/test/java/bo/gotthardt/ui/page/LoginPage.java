package bo.gotthardt.ui.page;

import bo.gotthardt.test.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

public class LoginPage extends PageObject<LoginPage> {
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

        return new DashboardPage(driver).get();
    }

    public LoginPage loginWithWrongCredentials(String name, String pass) {
        username.sendKeys(name);
        password.sendKeys(pass);
        submitButton.click();

        waitFor(wrongPasswordAlert, "Wrong password warning");
        isLoaded();

        return this;
    }

    @Override
    protected void load() {
        driver.get(BASE_URL);
    }

    @Override
    protected void isLoaded() throws Error {
        assertThat(username).isVisible();
        assertThat(password).isVisible();
    }
}
