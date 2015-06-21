package bo.gotthardt.todolist.application;

import bo.gotthardt.model.Widget;
import bo.gotthardt.rest.CrudService;
import com.avaje.ebean.EbeanServer;
import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;

public class WidgetCrudFactory implements Factory<CrudService<Widget>> {
    private final EbeanServer db;

    @Inject
    public WidgetCrudFactory(EbeanServer db) {
        this.db = db;
    }

    @Override
    public CrudService<Widget> provide() {
        return new CrudService<>(Widget.class, db);
    }

    @Override
    public void dispose(CrudService<Widget> instance) {

    }
}
