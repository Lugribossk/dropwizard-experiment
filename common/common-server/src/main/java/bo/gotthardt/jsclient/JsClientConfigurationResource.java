package bo.gotthardt.jsclient;

import bo.gotthardt.access.GlobalFeature;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Resource for serving the configuration values for a Javascript client.
 */
@Path("/configurations")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class JsClientConfigurationResource {
    private final JsClientConfiguration config;

    @GET
    @Path("/client")
    public JsClientConfiguration getConfiguration() {
        return config;
    }

    @GET
    @Path("/features")
    public Set<GlobalFeature> getGlobalFeatures() {
        return GlobalFeature.getEnabled();
    }
}
