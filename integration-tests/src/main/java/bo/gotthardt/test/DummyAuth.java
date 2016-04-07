package bo.gotthardt.test;

import bo.gotthardt.model.User;
import io.dropwizard.auth.Auth;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.security.Principal;

public class DummyAuth extends AbstractValueFactoryProvider {
    private static User user;

    @Inject
    public DummyAuth(MultivaluedParameterExtractorProvider mpep, ServiceLocator locator) {
        super(mpep, locator, Parameter.Source.UNKNOWN);
    }

    @Override
    protected Factory<?> createValueFactory(Parameter parameter) {
        if (!parameter.isAnnotationPresent(Auth.class)) {
            return null;
        }

        return new AbstractContainerRequestValueFactory<Principal>() {
            public Principal provide() {
                return user;
            }
        };
    }

    public static void setUser(User newUser) {
        user = newUser;
    }

    public static class Binder extends AbstractBinder {
        @Override
        protected void configure() {
            bind(DummyAuth.class).to(ValueFactoryProvider.class).in(Singleton.class);
        }
    }
}
