package bo.gotthardt.test.assertj;

import org.assertj.core.internal.Booleans;
import org.openqa.selenium.WebElement;

public class WebElementAssert extends BaseAssert<WebElementAssert, WebElement> {
    private Booleans booleans = Booleans.instance();

    protected WebElementAssert(WebElement actual) {
        super(actual, WebElementAssert.class);
    }

    public WebElementAssert isVisible() {
        booleans.assertEqual(info, actual.isDisplayed(), true); // TODO Is it supposed to work like this?
        return this;
    }

    public WebElementAssert containsText(String actual) {
        compare(this.actual.getText(), actual, "Element text");
        return this;
    }
}
