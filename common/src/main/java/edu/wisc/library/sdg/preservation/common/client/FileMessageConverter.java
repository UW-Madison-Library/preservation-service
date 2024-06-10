package edu.wisc.library.sdg.preservation.common.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * RestTemplate plugin for streaming a response to a File in a temp directory
 */
public class FileMessageConverter extends AbstractHttpMessageConverter<File> {

    private static final Logger LOG = LoggerFactory.getLogger(FileMessageConverter.class);

    private final Path tempDir;

    public FileMessageConverter(Path tempDir) {
        this.tempDir = tempDir;
    }

    @Override
    protected boolean canRead(MediaType mediaType) {
        return true;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz == File.class;
    }

    @Override
    protected File readInternal(Class<? extends File> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Files.createDirectories(tempDir);
        var path = tempDir.resolve(UUID.randomUUID().toString());
        LOG.debug("Downloading file to {}", path);
        Files.copy(inputMessage.getBody(), path);
        return path.toFile();
    }

    @Override
    protected void writeInternal(File file, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        throw new UnsupportedOperationException("not supported!");
    }
}
