package bo.gotthardt.util.assertj;

import com.sun.jersey.api.client.ClientResponse;
import org.assertj.core.api.Assertions;

/**
 * @author Bo Gotthardt
 */
public class DropwizardAssertions extends Assertions {
    public static ClientResponseAssert assertThat(ClientResponse actual) {
        return new ClientResponseAssert(actual);
    }
}
