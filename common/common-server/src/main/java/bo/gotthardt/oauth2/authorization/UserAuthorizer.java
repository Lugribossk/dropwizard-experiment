package bo.gotthardt.oauth2.authorization;

import bo.gotthardt.model.User;
import io.dropwizard.auth.Authorizer;

public class UserAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        return true;
    }
}
