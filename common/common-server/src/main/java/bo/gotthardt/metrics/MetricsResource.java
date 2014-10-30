package bo.gotthardt.metrics;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.Resources;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Bo Gotthardt
 */
@Path("/metrics")
public class MetricsResource {
    private final MetricRegistry metrics;
    private final String gif;

    @Inject
    public MetricsResource(MetricRegistry metrics) {
        this.metrics = metrics;

        try {
            this.gif = Resources.toString(Resources.getResource("bo/gotthardt/metrics/1x1.gif"), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("/meter/{name}")
    public void meter(@PathParam("name") String name, @QueryParam("value") Optional<Long> value) {
        metrics.meter(name).mark(value.or(1L));
    }

    @POST
    @Path("/timer/{name}")
    public void timer(@PathParam("name") String name, @QueryParam("value") long value) {
        metrics.timer(name).update(value, TimeUnit.MILLISECONDS);
    }

    @GET
    @Path("/meter/{name}.gif")
    @Produces("image/gif")
    public String meterGif(@PathParam("name") String name, @QueryParam("value") Optional<Long> value) {
        metrics.meter(name).mark(value.or(1L));
        return gif;
    }

    @GET
    @Path("/timer.gif")
    @Produces("image/gif")
    public String timerGif(@QueryParam("name") String name, @QueryParam("value") long value)  {
        metrics.timer(name).update(value, TimeUnit.MILLISECONDS);
        return gif;
    }
}
