package edu.wisc.library.sdg.preservation.worker.tools.fits;

import java.nio.file.Path;

public interface Fits {
    /**
     * Executes FITS on all of the files in the input dir and writes the FITS reports to the output dir.
     * This will fail if FITS is not installed on the system.
     *
     * @param inputDir  directory containing files to process
     * @param outputDir directory to write FITS reports to
     */
    void execute(Path inputDir, Path outputDir);

    /**
     * Indicates if FITS can be found.
     *
     * @return true if FITS is installed
     */
    boolean exists();

    /**
     * Indicates if FITS can be found. May use a cached response for performance.
     *
     * @return true if FITS is installed
     */
    boolean cachedExists();
}
