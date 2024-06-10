package edu.wisc.library.sdg.preservation.worker.tools.fits;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * FITS integration that makes HTTP requests to a FITS web server
 */
public class FitsWeb implements Fits {

    private static final Logger LOG = LoggerFactory.getLogger(FitsWeb.class);

    private final String fitsBaseUrl;
    private final String fitsUrlTmpl;
    private final HttpClient httpClient;
    private Boolean exists;

    public FitsWeb(String fitsUrl) {
        fitsBaseUrl = ArgCheck.notBlank(fitsUrl, "fitsUrl");
        fitsUrlTmpl = fitsUrl + "/process?inputDir=%s&outputDir=%s";
        httpClient = HttpClient.newHttpClient();
    }

    @Override
    public void execute(Path inputDir, Path outputDir) {
        var inputStr = inputDir.toAbsolutePath().toString();
        var outputStr = outputDir.toAbsolutePath().toString();

        var request = HttpRequest.newBuilder()
                .uri(createUri(inputStr, outputStr))
                .GET().build();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                // Remove partial FITS reports
                FileUtils.cleanDirectory(outputDir.toFile());
                throw new RuntimeException(String.format("Fits analysis failed: %s", response.body()));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists() {
        exists = null;
        checkExists();
        return exists;
    }

    @Override
    public boolean cachedExists() {
        if (exists == null) {
            checkExists();
        }
        return exists;
    }

    private synchronized void checkExists() {
        if (exists == null) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(fitsBaseUrl + "/ping"))
                    .GET().build();

            try {
                var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                exists = response.statusCode() == 200 && "fits-web".equals(response.body());
            } catch (Exception e) {
                LOG.error("Failed to connect to fits-web", e);
                exists = false;
            }
        }
    }

    private URI createUri(String inputDir, String outputDir) {
        return URI.create(String.format(fitsUrlTmpl,
                URLEncoder.encode(inputDir, StandardCharsets.UTF_8),
                URLEncoder.encode(outputDir, StandardCharsets.UTF_8)));
    }

}
