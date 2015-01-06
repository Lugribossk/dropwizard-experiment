package bo.gotthardt.test.assertj;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;

import javax.ws.rs.core.Response;

/**
 * @author Bo Gotthardt
 */
public class DropwizardAssertions extends Assertions {
    public static ResponseAssert assertThat(Response actual) {
        return new ResponseAssert(actual);
    }

    public static WebElementAssert assertThat(WebElement actual) {
        return new WebElementAssert(actual);
    }
}
