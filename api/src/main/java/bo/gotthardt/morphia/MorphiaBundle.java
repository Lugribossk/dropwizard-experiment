package bo.gotthardt.morphia;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.MongoClient;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import lombok.Getter;

/**
 * @author Bo Gotthardt
 */
@Getter
public class MorphiaBundle implements ConfiguredBundle<HasMongoConfiguration> {
    private MongoClient mongoClient;
    private Morphia morphia;
    private Datastore datastore;


    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    @Override
    public void run(HasMongoConfiguration configuration, Environment environment) throws Exception {
        mongoClient = new MongoClient(configuration.getMongodb().getHost(), configuration.getMongodb().getPort());

        morphia = new Morphia();
        morphia.mapPackage("bo.gotthardt.model", true);
        datastore = morphia.createDatastore(mongoClient, "dbname");
        datastore.ensureIndexes();
        datastore.ensureCaps();
    }
}
