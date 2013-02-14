package bo.gotthardt.util.fest;

import com.sun.jersey.api.client.ClientResponse;
import org.fest.assertions.api.Assertions;

/**
 * @author Bo Gotthardt
 */
public class DropwizardAssertions extends Assertions {
    public static ClientResponseAssert assertThat(ClientResponse actual) {
        return new ClientResponseAssert(actual);
    }
}
