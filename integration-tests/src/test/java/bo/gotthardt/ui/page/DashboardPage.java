package bo.gotthardt.ui.page;

import bo.gotthardt.test.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

public class DashboardPage extends PageObject {
    @FindBy(tagName = "h1")
    private WebElement header;
    @FindBy(className = "logout")
    private WebElement logout;
    @FindBy(className = "name-dropdown")
    private WebElement userFullName;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() throws Error {
        waitFor(header, "Header");
        assertThat(header).containsText("Dashboard");
    }

    public LoginPage logout() {
        userFullName.click();
        logout.click();
        return new LoginPage(driver);
    }

    public String getUserFullName() {
        return userFullName.getText();
    }

    public static DashboardPage go(WebDriver driver) {
        driver.get(BASE_URL + "");
        return new DashboardPage(driver);
    }
}
