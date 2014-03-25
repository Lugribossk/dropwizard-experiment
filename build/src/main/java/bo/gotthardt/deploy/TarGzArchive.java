package bo.gotthardt.deploy;

import org.codehaus.plexus.archiver.gzip.GZipArchiver;
import org.codehaus.plexus.archiver.tar.TarArchiver;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class TarGzArchive {
    /**
     * Create a .tar.gz archive file that contains the specified files.
     * The archive will be located in a temporary folder.
     *
     * @param files The files
     * @param folder The folder name to place the files in, inside the archive.
     * @return The archive
     * @throws IOException
     */
    public static File create(Set<File> files, String folder) throws IOException {
        return gz(tar(files, folder));
    }

    private static File tar(Set<File> files, String folder) throws IOException {
        TarArchiver tarArchive = new TarArchiver();
        tarArchive.enableLogging(new ConsoleLogger(Logger.LEVEL_DISABLED, "console"));
        for (File file : files) {
            tarArchive.addFile(file, folder + "/" + file.getName());
        }

        File tarFile = File.createTempFile("TarGzArchive", ".tar");
        tarArchive.setDestFile(tarFile);
        tarArchive.createArchive();

        return tarFile;
    }

    private static File gz(File input) throws IOException {
        GZipArchiver gzipArchive = new GZipArchiver();
        gzipArchive.addFile(input, input.getName());

        File gzFile = File.createTempFile("TarGzArchive", ".tar.gz");
        gzipArchive.setDestFile(gzFile);
        gzipArchive.createArchive();
        
        return gzFile;
    }
}
