package bo.gotthardt.ebean;

import com.avaje.ebean.EbeanServer;
import com.codahale.metrics.health.HealthCheck;
import lombok.RequiredArgsConstructor;

/**
 *
 *
 * @author Bo Gotthardt
 */
@RequiredArgsConstructor
public class EbeanHealthCheck extends HealthCheck {
    private final EbeanServer db;

    @Override
    protected Result check() throws Exception {
        db.createSqlQuery("/* EbeanHealthCheck */ SELECT 1").findUnique();
        return Result.healthy();
    }
}
