package bo.gotthardt.ebean;

import com.avaje.ebean.EbeanServer;
import com.yammer.metrics.core.HealthCheck;

/**
 *
 *
 * @author Bo Gotthardt
 */
public class EbeanHealthCheck extends HealthCheck {
    private final EbeanServer ebean;

    public EbeanHealthCheck(EbeanServer ebean) {
        super("ebean-" + ebean.getName());
        this.ebean = ebean;
    }

    @Override
    protected Result check() throws Exception {
        ebean.createSqlQuery("/* EbeanHealthCheck */ SELECT 1").findUnique();
        return Result.healthy();
    }
}
