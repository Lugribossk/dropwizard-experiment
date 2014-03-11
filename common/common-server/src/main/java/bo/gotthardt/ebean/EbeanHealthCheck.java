package bo.gotthardt.ebean;

import com.avaje.ebean.EbeanServer;
import com.codahale.metrics.health.HealthCheck;

/**
 *
 *
 * @author Bo Gotthardt
 */
public class EbeanHealthCheck extends HealthCheck {
    private final EbeanServer ebean;

    public EbeanHealthCheck(EbeanServer ebean) {
        this.ebean = ebean;
    }

    @Override
    protected Result check() throws Exception {
        ebean.createSqlQuery("/* EbeanHealthCheck */ SELECT 1").findUnique();
        return Result.healthy();
    }
}
