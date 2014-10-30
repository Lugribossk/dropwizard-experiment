package bo.gotthardt.email;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.util.Map;

/**
 * Service for sending emails.
 *
 * @author Bo Gotthardt
 */
public interface EmailService {
    /**
     * Send email.
     *
     * @param toAddress The email address to send to.
     * @param subject The subject.
     * @param htmlContent The message as an HTML string.
     */
    public void send(String toAddress, String subject, String htmlContent);

    /**
     * Send email template.
     * The template is a simple Handlebars-like format with '{{name}}' expressions.
     *
     * @param toAddress The email address to send to.
     * @param subject The subject.
     * @param templateName The name of a resource file to use as pseudo-Handlebars template.
     * @param data The template data, as a map of template expression names to their values.
     */
    default public void sendTemplate(String toAddress, String subject, String templateName, Map<String, String> data) {
        String content;
        try {
            content = Resources.toString(Resources.getResource(templateName), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String key : data.keySet()) {
            content = content.replace("{{" + key + "}}", data.get(key));
        }

        send(toAddress, subject, content);
    }
}
