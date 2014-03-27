package bo.gotthardt.todo;

import io.dropwizard.assets.AssetsBundle;

/**
 * @author Bo Gotthardt
 */
public class TodoClientBundle extends AssetsBundle {
    // Uploading the client-side assets to S3 is a better long-term solution, but this will do for now.
    public TodoClientBundle() {
        super("/bo/gotthardt/todo/client", "/static", "index.html");
    }
}
