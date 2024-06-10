package edu.wisc.library.sdg.preservation.common.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

/**
 * Utility for creating and extracting zip archives
 */
public final class Archiver {

    private Archiver() {

    }

    /**
     * Recursively create a zip archive
     *
     * @param source the file or directory to zip
     * @param destination the path to the zip file to create
     * @param compress true if the zip should be compressed
     */
    public static void archive(Path source, Path destination, boolean compress) {
        ArgCheck.notNull(source, "source");
        ArgCheck.notNull(destination, "destination");

        try (var zipOut = new ZipArchiveOutputStream(destination)) {
            if (!compress) {
                zipOut.setLevel(Deflater.NO_COMPRESSION);
            }

             var rootParent = source.getParent();

            try (var files = Files.walk(source)) {
                files.forEach(file -> {
                    try {
                        var relativePathStr = rootParent.relativize(file).toString();
                        ZipArchiveEntry entry = (ZipArchiveEntry) zipOut.createArchiveEntry(file.toFile(), relativePathStr);

                        if (!compress) {
                            entry.setMethod(ZipEntry.STORED);
                        }

                        zipOut.putArchiveEntry(entry);

                        if (Files.isRegularFile(file)) {
                            Files.copy(file, zipOut);
                        }

                        zipOut.closeArchiveEntry();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            }

             zipOut.finish();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Extracts a zip archive to the specified location
     *
     * @param source the zip archive to extract
     * @param destination the location to extract to
     */
    public static void extract(Path source, Path destination) {
        ArgCheck.notNull(source, "source");
        ArgCheck.notNull(destination, "destination");

        try (var zipFile = new ZipFile(source.toFile())) {

            for (var it = zipFile.getEntries().asIterator(); it.hasNext();) {
                var entry = it.next();

                var destinationFile = destination.resolve(entry.getName());

                if (entry.isDirectory()) {
                    if (Files.notExists(destinationFile)) {
                        Files.createDirectories(destinationFile);
                    }
                } else {
                    if (Files.notExists(destinationFile.getParent())) {
                        Files.createDirectories(destinationFile.getParent());
                    }

                    Files.copy(zipFile.getInputStream(entry), destinationFile);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
