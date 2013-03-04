package bo.gotthardt.ebean;

import com.avaje.ebean.Ebean;
import com.yammer.metrics.core.HealthCheck;

/**
 *
 *
 * @author Bo Gotthardt
 */
public class EbeanHealthCheck extends HealthCheck {
    public EbeanHealthCheck() {
        super("ebean");
    }

    @Override
    protected Result check() throws Exception {
        Ebean.createSqlQuery("/* Health Check */ SELECT 1").findUnique();
        return Result.healthy();
    }
}
