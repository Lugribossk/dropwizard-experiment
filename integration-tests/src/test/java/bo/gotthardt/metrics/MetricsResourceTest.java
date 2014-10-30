package bo.gotthardt.metrics;

import bo.gotthardt.test.ApiIntegrationTest;
import com.codahale.metrics.MetricRegistry;
import com.google.common.net.MediaType;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;

/**
 * Tests for {@link bo.gotthardt.metrics.MetricsResource}.
 */
public class MetricsResourceTest extends ApiIntegrationTest {
    private static final MetricRegistry metrics = new MetricRegistry();
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new MetricsResource(metrics))
            .build();

    @Before
    public void clearMetrics() {
        metrics.getNames().forEach(metrics::remove);
    }

    @Test
    public void shouldIncreaseMeterByOneByDefault() {
        assertThat(POST("/metrics/meter/test", null)).hasStatus(Response.Status.NO_CONTENT);
        assertThat(metrics.meter("test").getCount()).isEqualTo(1);
    }

    @Test
    public void shouldIncreaseMeterByQueryValue() {
        assertThat(POST("/metrics/meter/test?value=5", null)).hasStatus(Response.Status.NO_CONTENT);
        assertThat(metrics.meter("test").getCount()).isEqualTo(5);
    }

    @Test
    public void shouldUpdateTimerWithQueryValue() {
        assertThat(POST("/metrics/timer/test?value=1000", null)).hasStatus(Response.Status.NO_CONTENT);
        assertThat(metrics.timer("test").getSnapshot().getValues()[0]).isEqualTo(TimeUnit.MILLISECONDS.toNanos(1000));
    }

    @Test
    public void shouldReturnGif() {
        assertThat(GET("/metrics/meter/test.gif?value=1"))
                .hasStatus(Response.Status.OK)
                .hasContentType(MediaType.GIF);
        assertThat(metrics.meter("test").getCount()).isEqualTo(1);
    }

    @Override
    public ResourceTestRule getResources() {
        return resources;
    }
}