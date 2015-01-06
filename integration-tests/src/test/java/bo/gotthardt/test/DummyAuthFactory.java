package bo.gotthardt.test;

import bo.gotthardt.model.User;
import io.dropwizard.auth.AuthFactory;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bo Gotthardt
 */
@Setter
public class DummyAuthFactory extends AuthFactory<Void, User> {
    private User user;

    public DummyAuthFactory() {
        super(null);
    }

    @Override
    public User provide() {
        return user;
    }

    @Override
    public void setRequest(HttpServletRequest request) {

    }

    @Override
    public AuthFactory<Void, User> clone(boolean required) {
        return this;
    }

    @Override
    public Class<User> getGeneratedClass() {
        return User.class;
    }

}
