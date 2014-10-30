package bo.gotthardt.test.assertj;

import com.sun.jersey.api.client.ClientResponse;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;

/**
 * @author Bo Gotthardt
 */
public class DropwizardAssertions extends Assertions {
    public static ClientResponseAssert assertThat(ClientResponse actual) {
        return new ClientResponseAssert(actual);
    }

    public static WebElementAssert assertThat(WebElement actual) {
        return new WebElementAssert(actual);
    }
}
