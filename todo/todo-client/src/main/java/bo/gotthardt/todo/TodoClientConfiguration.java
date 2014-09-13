package bo.gotthardt.todo;

import bo.gotthardt.jsclient.JsClientConfiguration;
import lombok.Getter;

@Getter
public class TodoClientConfiguration implements JsClientConfiguration {
    private String googleAnalytics = "";
}
