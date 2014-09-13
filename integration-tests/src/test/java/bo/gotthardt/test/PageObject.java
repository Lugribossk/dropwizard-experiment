package bo.gotthardt.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class PageObject {
    // Set in integration.yml
    protected static String BASE_URL = "http://localhost:8056/";

    protected WebDriver driver;

    protected PageObject(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
        onLoad();
    }

    /**
     * Called when the page is presumably about to load.
     * The implementation for a page should therefore first <b>wait</b> for elements the page contains,
     * and then <b>assert</b> that they have the correct content.
     */
    protected abstract void onLoad();

    /**
     * Wait for the specified element to be visible.
     *
     * @param locator The element locator
     * @param message The message to display on failure.
     */
    protected void waitFor(By locator, String message) {
        new WebDriverWait(driver, 5)
                .withMessage(message)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for the specified element to be visible.
     *
     * @param element The element
     * @param message The message to display on failure.
     */
    protected void waitFor(WebElement element, String message) {
        new WebDriverWait(driver, 5)
                .withMessage(message)
                .until(ExpectedConditions.visibilityOf(element));
    }

}
