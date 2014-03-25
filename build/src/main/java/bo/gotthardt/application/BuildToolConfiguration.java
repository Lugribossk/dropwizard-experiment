package bo.gotthardt.application;

import bo.gotthardt.deploy.HerokuCredentials;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildToolConfiguration extends Configuration {
    private HerokuCredentials heroku;
}
