package bo.gotthardt.util;

import bo.gotthardt.jersey.provider.AbstractInjectableProvider;
import bo.gotthardt.model.User;
import com.sun.jersey.api.core.HttpContext;
import io.dropwizard.auth.Auth;
import lombok.Setter;

/**
 * @author Bo Gotthardt
 */
@Setter
public class DummyAuthProvider extends AbstractInjectableProvider<Auth, User> {
    private User user;

    public DummyAuthProvider() {
        super(User.class);
    }

    @Override
    public User getValue(HttpContext httpContext) {
        return user;
    }
}
