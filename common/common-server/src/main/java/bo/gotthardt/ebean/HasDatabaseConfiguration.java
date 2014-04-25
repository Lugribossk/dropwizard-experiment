package bo.gotthardt.ebean;

import io.dropwizard.db.DataSourceFactory;

/**
 * @author Bo Gotthardt
 */
public interface HasDatabaseConfiguration {
    public DataSourceFactory getDatabase();
}
