package bo.gotthardt.application;

import bo.gotthardt.model.Person;
import com.avaje.ebean.Ebean;
import com.yammer.metrics.core.HealthCheck;

/**
 * @author Bo Gotthardt
 */
public class EbeanHealthCheck extends HealthCheck {

    public EbeanHealthCheck() {
        super("ebean");
    }

    @Override
    protected Result check() throws Exception {
        Ebean.find(Person.class).findRowCount();
        return Result.healthy();
    }
}
