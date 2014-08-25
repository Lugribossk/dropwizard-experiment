package bo.gotthardt.ebean;

import com.google.common.collect.Sets;
import org.reflections.Reflections;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.Set;

/**
 * Utility class for finding the classes that Ebean should enhance.
 */
public class EbeanEntities {
    public static Set<Class> getEntities() {
        Reflections reflections = new Reflections("bo.gotthardt.model");
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
        Set<Class<?>> embeddables = reflections.getTypesAnnotatedWith(Embeddable.class);

        return Sets.union(embeddables, entities);
    }
}
