package bo.gotthardt.ui;

import bo.gotthardt.model.User;
import bo.gotthardt.test.UiIntegrationTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Bo Gotthardt
 */
public class BlahUiTest extends UiIntegrationTest {
    @Test
    public void blah() {
        driver.get("http://localhost:8080/");

        assertThat(driver.getTitle()).isEqualTo("Backbone stuff");
    }

    @Test
    public void blah2() {
        db.save(new User("test2", "test2"));
        driver.get("http://localhost:8080/");

        driver.findElement(By.id("username")).sendKeys("test2");
        driver.findElement(By.id("password")).sendKeys("test2");
        driver.findElement(By.className("btn-primary")).click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.tagName("h1")));
        assertThat(driver.findElement(By.tagName("h1")).getText()).isEqualTo("Dashboard");
    }
}
