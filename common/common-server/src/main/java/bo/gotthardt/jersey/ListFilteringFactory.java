package bo.gotthardt.jersey;

import bo.gotthardt.jersey.provider.ListFiltering;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;

/**
 * @author Bo Gotthardt
 */
public class ListFilteringFactory extends AbstractContainerRequestValueFactory<ListFiltering> {

    @Override
    public ListFiltering provide() {
        return new ListFiltering();
    }
}
