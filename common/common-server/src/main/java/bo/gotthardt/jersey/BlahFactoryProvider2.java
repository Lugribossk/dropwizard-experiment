package bo.gotthardt.jersey;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Context;

/**
 * @author Bo Gotthardt
 */
@Singleton
public class BlahFactoryProvider2<V> extends AbstractValueFactoryProvider {
    private Factory<V> factory;
    @Inject
    public BlahFactoryProvider2(final MultivaluedParameterExtractorProvider extractorProvider,
                               final Factory<V> factory,
                               final ServiceLocator injector) {
        super(extractorProvider, injector, Parameter.Source.UNKNOWN);
        this.factory = factory;
    }

    @Override
    protected Factory<?> createValueFactory(final Parameter parameter) {
        //final Class<?> classType = parameter.getRawType();
        final Context auth = parameter.getAnnotation(Context.class);

        if (auth != null /*&& classType.isAssignableFrom(factory.getGeneratedClass())*/) {
            return factory;
        } else {
            return null;
        }
    }

    @Singleton
    public static class BlahInjectionResolver extends ParamInjectionResolver<Context> {
        public BlahInjectionResolver() {
            super(BlahFactoryProvider2.class);
        }
    }

    public static class BlahBinder<V> extends AbstractBinder {
        private Factory<V> factory;

        public BlahBinder(Factory<V> factory) {
            this.factory = factory;
        }

        @Override
        protected void configure() {
            bind(this.factory).to(Factory.class);
            bind(BlahFactoryProvider2.class).to(ValueFactoryProvider.class).in(Singleton.class);
            bind(BlahInjectionResolver.class).to(
                new TypeLiteral<InjectionResolver<Context>>() {
                }
            ).in(Singleton.class);
        }
    }

    public static <V> Binder binder (Factory<V> factory) {
        return new BlahBinder<>(factory);
    }

}
