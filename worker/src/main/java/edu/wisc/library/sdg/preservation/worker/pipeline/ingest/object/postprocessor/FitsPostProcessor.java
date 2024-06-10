package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.postprocessor;

import java.nio.file.Path;

public interface FitsPostProcessor {
    void process(Path fitsOutputPath, String fitsFile, String outputFile);
    void process(Path fitsOutputPath, String fitsFile);
}
