package bo.gotthardt.email;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

public class EmailServiceTest {
    @Test
    public void shouldDoCrappyTemplating() {
        TestEmailService email = new TestEmailService();
        Map<String, String> data = ImmutableMap.of("name", "World");

        email.sendTemplate("test@example.com", "Test", "bo\\gotthardt\\email\\HelloWorld.hbs", data);

        assertThat(email.htmlContent).isEqualTo("Hello World!");
    }

    private static class TestEmailService implements EmailService {
        private String htmlContent;

        @Override
        public void send(String toAddress, String subject, String htmlContent) {
            this.htmlContent = htmlContent;
        }
    }
}