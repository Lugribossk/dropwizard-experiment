package bo.gotthardt.deploy;

import com.google.common.collect.ImmutableMap;
import jp.co.flect.heroku.platformapi.PlatformApi;
import jp.co.flect.heroku.platformapi.model.App;
import jp.co.flect.heroku.platformapi.model.Release;
import jp.co.flect.heroku.platformapi.model.Slug;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link bo.gotthardt.deploy.HerokuDeployer}.
 *
 * @author Bo Gotthardt
 */
public class HerokuDeployerTest {
    private File jarFile;
    private File configFile;

    @Before
    public void setup() throws IOException {
        jarFile = File.createTempFile("test", ".jar");
        configFile = File.createTempFile("test", ".yml");
        jarFile.deleteOnExit();
        configFile.deleteOnExit();
    }

    @Test
    public void shouldReleaseAppWithSlug() throws IOException {
        PlatformApi heroku = mock(PlatformApi.class);
        when(heroku.getApp(Matchers.any()))
                .thenReturn(new App());
        when(heroku.createSlug(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any()))
                .thenReturn(new Slug(ImmutableMap.of("id", "slugid")));
        when(heroku.createRelease(Matchers.any(), Matchers.any(), Matchers.any()))
                .thenReturn(new Release());

        HerokuDeployer deployer = new HerokuDeployer(heroku);
        deployer.deploy("testapp", File.createTempFile("test", ".jar"), File.createTempFile("test", ".yml"), "test");

        verify(heroku).createRelease("testapp", "slugid", "HerokuDeployer");
    }
}
