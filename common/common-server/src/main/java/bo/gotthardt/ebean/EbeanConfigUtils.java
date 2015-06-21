package bo.gotthardt.ebean;

import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import io.dropwizard.db.DataSourceFactory;

public class EbeanConfigUtils {
    public static ServerConfig createServerConfig(DataSourceFactory dbConfig) {
        ServerConfig config = new ServerConfig();
        config.setName("main");
        config.setDataSourceConfig(createDataSourceConfig(dbConfig));
        config.setDefaultServer(true);

        EbeanEntities.getEntities().forEach(config::addClass);

        return config;
    }

    public static DataSourceConfig createDataSourceConfig(DataSourceFactory dbConfig) {
        DataSourceConfig config = new DataSourceConfig();
        config.setUsername(dbConfig.getUser());
        config.setPassword(dbConfig.getPassword());
        config.setUrl(dbConfig.getUrl());
        config.setDriver(dbConfig.getDriverClass());
        config.setMinConnections(dbConfig.getMinSize());
        config.setMaxConnections(dbConfig.getMaxSize());

        return config;
    }

    public static DataSourceFactory clone(DataSourceFactory dbConfig) {
        DataSourceFactory newConfig = new DataSourceFactory();
        newConfig.setUser(dbConfig.getUser());
        newConfig.setPassword(dbConfig.getPassword());
        newConfig.setUrl(dbConfig.getUrl());
        newConfig.setDriverClass(dbConfig.getDriverClass());
        newConfig.setMaxSize(dbConfig.getMaxSize());
        newConfig.setMinSize(dbConfig.getMinSize());
        newConfig.setInitialSize(dbConfig.getInitialSize());

        return newConfig;
    }
}
