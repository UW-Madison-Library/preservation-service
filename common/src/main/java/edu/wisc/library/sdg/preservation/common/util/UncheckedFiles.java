package edu.wisc.library.sdg.preservation.common.util;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UncheckedFiles {

    private UncheckedFiles() {
    }

    public static Path createDirectories(Path path) {
        try {
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void deleteDirectory(Path path) {
        if (Files.exists(path)) {
            try {
                MoreFiles.deleteRecursively(path, RecursiveDeleteOption.ALLOW_INSECURE);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    public static void delete(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void move(Path src, Path dst, CopyOption... options) {
        try {
            Files.move(src, dst, options);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static long size(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
