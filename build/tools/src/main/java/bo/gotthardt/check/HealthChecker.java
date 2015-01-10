package bo.gotthardt.check;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.HttpHostConnectException;

import java.util.concurrent.TimeUnit;

/**
 * @author Bo Gotthardt
 */
@Slf4j
@RequiredArgsConstructor
public class HealthChecker {
    private static final int DELAY = 5;
    private final String url;
    private final long maxDuration;
    private long duration = 0;

    public boolean check() throws InterruptedException {
        try {
            HttpResponse<String> health = Unirest.get(url + "/admin/healthcheck")
                    .basicAuth("test", "test")
                    .asString();

            if (health.getCode() == 200) {
                log.info("Healthy with {}", health.getBody());
                return true;
            } else {
                log.error("Unhealthy with {}", health.getBody());
                return false;
            }
        } catch (UnirestException e) {
            if (e.getCause() instanceof HttpHostConnectException && duration < maxDuration) {
                log.info("Unable to connect, retrying...");
                duration = duration + DELAY;
                Thread.sleep(TimeUnit.SECONDS.toMillis(DELAY));
                return check();
            }
            log.error("Unable to connect.", e);
            return false;
        }
    }
}
