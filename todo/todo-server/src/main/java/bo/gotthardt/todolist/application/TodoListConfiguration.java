package bo.gotthardt.todolist.application;

import bo.gotthardt.ebean.HasDatabaseConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;

/**
 * @author Bo Gotthardt
 */
@Getter
public class TodoListConfiguration extends Configuration implements HasDatabaseConfiguration {
    private DataSourceFactory database;
}
