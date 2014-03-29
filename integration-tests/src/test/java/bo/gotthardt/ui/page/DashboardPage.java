package bo.gotthardt.ui.page;

import bo.gotthardt.test.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

public class DashboardPage extends PageObject<DashboardPage> {
    @FindBy(tagName = "h1")
    private WebElement header;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void load() {
        waitFor(By.tagName("h1"), "Header");
    }

    @Override
    protected void isLoaded() throws Error {
        assertThat(header).containsText("Dashboard");
    }
}
