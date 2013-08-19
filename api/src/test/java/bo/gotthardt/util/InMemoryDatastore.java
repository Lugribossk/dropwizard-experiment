package bo.gotthardt.util;

import com.foursquare.fongo.Fongo;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.MongoClient;
import com.mongodb.MongoOptions;
import lombok.Delegate;
import org.fest.reflect.core.Reflection;

/**
 * @author Bo Gotthardt
 */
public class InMemoryDatastore implements Datastore {

    @Delegate(types = Datastore.class)
    private final Datastore datastore;

    public InMemoryDatastore() {
        Fongo fongo = new Fongo("test");
        Morphia morphia = new Morphia();
        morphia.mapPackage("bo.gotthardt.model", true);

        MongoClient mongo = fongo.getMongo();
        // There seems to be a problem with Fongo where its internal mock MongoClient ends up with a null options, despite setting it.
        Reflection.field("options").ofType(MongoOptions.class).in(mongo).set(new MongoOptions());

        datastore = morphia.createDatastore(mongo, "dbname");
    }
}
