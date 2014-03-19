package bo.gotthardt;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * @author Bo Gotthardt
 */
public class TOTPSecretTest {
    @Test
    public void blah() {
        TOTPSecret x = new TOTPSecret();

        DateTime now = DateTime.now();

        //int previousToken = x.tokenAt(now.minusSeconds(30).getMillis() / 30);
        int currentToken = x.tokenAt(now.getMillis() / 1000 / 30);
//        int nextToken = x.tokenAt(now.plusSeconds(30).getMillis() / 30);

//        System.out.println(previousToken);
        System.out.println(currentToken);
//        System.out.println(nextToken);
    }
}
