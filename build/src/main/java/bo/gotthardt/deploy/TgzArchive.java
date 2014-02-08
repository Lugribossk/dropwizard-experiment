package bo.gotthardt.deploy;

import org.codehaus.plexus.archiver.gzip.GZipArchiver;
import org.codehaus.plexus.archiver.tar.TarArchiver;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class TgzArchive {
    public static File create(Set<File> files, String folder) throws IOException {
        GZipArchiver gzip = new GZipArchiver();
        for (File file : files) {
            gzip.addFile(file, folder + "/" + file.getName());
        }

        File gzFile = File.createTempFile("DeployCommand", null);
        gzip.setDestFile(gzFile);
        gzip.createArchive();

        TarArchiver tar = new TarArchiver();
        tar.addFile(gzFile, "blah");

        File tarFile = File.createTempFile("slug", "tgz");
        tar.setDestFile(tarFile);
        tar.createArchive();

        return tarFile;
    }
}
