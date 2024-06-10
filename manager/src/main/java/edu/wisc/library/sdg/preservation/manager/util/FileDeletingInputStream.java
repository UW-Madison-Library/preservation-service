package edu.wisc.library.sdg.preservation.manager.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDeletingInputStream extends InputStream {

    private final Path file;
    private final InputStream inner;

    public FileDeletingInputStream(Path file, InputStream inner) {
        this.file = file;
        this.inner = inner;
    }

    @Override
    public int read() throws IOException {
        return inner.read();
    }

    @Override
    public void close() throws IOException {
        try {
            inner.close();
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
