package bo.gotthardt.jersey.provider;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Abstract class for easily building Jersey injectable providers.
 *
 * @author Coda Hale (http://codahale.com/what-makes-jersey-interesting-injection-providers/)
 *         Slightly modified to take the annotation as a type parameter.
 */
public abstract class AbstractInjectableProvider<A extends Annotation, E>
        extends AbstractHttpContextInjectable<E>
        implements InjectableProvider<A, Type> {

    private final Type t;

    public AbstractInjectableProvider(Type t) {
        this.t = t;
    }

    @Override
    public Injectable<E> getInjectable(ComponentContext ic, A a, Type c) {
        if (c.equals(t)) {
            return getInjectable(ic, a);
        }

        return null;
    }

    public Injectable<E> getInjectable(ComponentContext ic, A a) {
        return this;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }
}
