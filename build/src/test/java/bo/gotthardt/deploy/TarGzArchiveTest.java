package bo.gotthardt.deploy;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TarGzArchive}.
 *
 * @author Bo Gotthardt
 */
public class TarGzArchiveTest {
    private File input;
    private File unpackDir;

    @Before
    public void setup() throws IOException {
        input = File.createTempFile("qwerty", ".txt");
        Files.append("test", input, Charsets.UTF_8);
        unpackDir = Files.createTempDir();
        input.deleteOnExit();
        unpackDir.deleteOnExit();
    }

    @Test
    public void shouldCreateTarGzArchiveWithInputFile() throws IOException {
        File output = TarGzArchive.create(ImmutableSet.of(input), "");

        assertThat(output).exists();
        assertThat(output.getName()).endsWith(".tgz");

        List<File> unpackedFiles = unpack(output);

        assertThat(unpackedFiles).hasSize(1);
        assertThat(unpackedFiles.get(0).getName()).isEqualTo(input.getName());
        assertThat(Files.toString(unpackedFiles.get(0), Charsets.UTF_8)).isEqualTo("test");
    }

    @Test
    public void shouldPlaceInputFileInSpecifiedFolder() throws IOException {
        File output = TarGzArchive.create(ImmutableSet.of(input), "testfolder");

        assertThat(output).exists();

        List<File> unpackedFiles = unpack(output);
        File folder = unpackedFiles.get(0);

        assertThat(folder).isDirectory();
        assertThat(folder.getName()).isEqualTo("testfolder");

        List<File> filesInsideFolder = ImmutableList.copyOf(folder.listFiles());

        assertThat(filesInsideFolder).hasSize(1);
        assertThat(filesInsideFolder.get(0).getName()).isEqualTo(input.getName());
    }

    private List<File> unpack(File tgzFile) {
        TarGZipUnArchiver unarchiver = new TarGZipUnArchiver(tgzFile);
        // Needed to avoid a null pointer...
        unarchiver.enableLogging(new ConsoleLogger(Logger.LEVEL_DISABLED, "console"));
        unarchiver.setDestDirectory(unpackDir);
        unarchiver.extract();

        return ImmutableList.copyOf(unpackDir.listFiles());
    }
}
