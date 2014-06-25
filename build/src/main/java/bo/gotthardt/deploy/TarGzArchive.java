package bo.gotthardt.deploy;

import com.google.common.io.Files;
import org.codehaus.plexus.archiver.gzip.GZipArchiver;
import org.codehaus.plexus.archiver.tar.TarArchiver;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class TarGzArchive {
    /**
     * Unpack a .tar.gz archive and return the folder it was unpacked in.
     *
     * @param tarGz The archive
     * @return The unpacked folder
     */
    public static File unpack(File tarGz) {
        File unpackDir = Files.createTempDir();
        unpackDir.deleteOnExit();

        TarGZipUnArchiver unArchiver = new TarGZipUnArchiver(tarGz);
        // Needed to avoid a null pointer...
        unArchiver.enableLogging(new ConsoleLogger(Logger.LEVEL_DISABLED, "console"));
        unArchiver.setDestDirectory(unpackDir);
        unArchiver.extract();

        return unpackDir;
    }

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
            // The starting ./ in the folder name is required for Heroku to be able to unpack the files correctly.
            if (file.isFile()) {
                tarArchive.addFile(file, "./" + folder + "/" + file.getName());
            } else if (file.isDirectory()) {
                tarArchive.addDirectory(file, "./" + folder + "/" + file.getName() + "/");
            }
        }

        File tarFile = File.createTempFile("TarGzArchive", ".tar");
        tarArchive.setDestFile(tarFile);
        tarArchive.createArchive();

        return tarFile;
    }

    private static File gz(File input) throws IOException {
        GZipArchiver gzipArchive = new GZipArchiver();
        gzipArchive.addFile(input, input.getName());

        File gzFile = File.createTempFile("TarGzArchive", ".tgz");
        gzipArchive.setDestFile(gzFile);
        gzipArchive.createArchive();
        
        return gzFile;
    }
}
