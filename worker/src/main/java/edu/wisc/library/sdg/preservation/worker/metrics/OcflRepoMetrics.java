package edu.wisc.library.sdg.preservation.worker.metrics;

import edu.wisc.library.sdg.preservation.common.metrics.Outcome;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.ocfl.api.OcflConfig;
import io.ocfl.api.OcflObjectUpdater;
import io.ocfl.api.OcflOption;
import io.ocfl.api.OcflRepository;
import io.ocfl.api.model.FileChangeHistory;
import io.ocfl.api.model.ObjectDetails;
import io.ocfl.api.model.ObjectVersionId;
import io.ocfl.api.model.OcflObjectVersion;
import io.ocfl.api.model.ValidationResults;
import io.ocfl.api.model.VersionDetails;
import io.ocfl.api.model.VersionInfo;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Metrics wrapper for ocfl-java
 */
public class OcflRepoMetrics implements OcflRepository {

    private final OcflRepository inner;
    private final String storeName;
    private final MeterRegistry registry;

    public OcflRepoMetrics(OcflRepository inner, String storeName, MeterRegistry registry) {
        this.inner = inner;
        this.storeName = storeName;
        this.registry = registry;
    }

    private void record(String operation, Runnable r) {
        var outcome = Outcome.SUCCESS;
        var timer = Timer.start();
        try {
            r.run();
        } catch (RuntimeException e) {
            outcome = Outcome.FAILURE;
            throw e;
        } finally {
            timer.stop(Timer.builder("ocfl")
                    .tag("method", operation)
                    .tag("store", storeName)
                    .tag(Outcome.NAME, outcome.toString())
                    .publishPercentileHistogram()
                    .register(registry));
        }
    }

    private <T> T record(String operation, Callable<T> c) {
        var outcome = Outcome.SUCCESS;
        var timer = Timer.start();
        try {
            return c.call();
        } catch (RuntimeException e) {
            outcome = Outcome.FAILURE;
            throw e;
        } catch (Exception e) {
            // not reachable
            outcome = Outcome.FAILURE;
            throw new RuntimeException(e);
        } finally {
            timer.stop(Timer.builder("ocfl")
                    .tag("method", operation)
                    .tag("store", storeName)
                    .tag(Outcome.NAME, outcome.toString())
                    .publishPercentileHistogram()
                    .register(registry));
        }
    }

    @Override
    public ObjectVersionId putObject(ObjectVersionId objectVersionId,
                                     Path path,
                                     VersionInfo versionInfo,
                                     OcflOption... ocflOptions) {
        return record("putObject", () -> inner.putObject(objectVersionId, path, versionInfo, ocflOptions));
    }

    @Override
    public ObjectVersionId updateObject(ObjectVersionId objectVersionId,
                                        VersionInfo versionInfo,
                                        Consumer<OcflObjectUpdater> consumer) {
        return record("updateObject", () -> inner.updateObject(objectVersionId, versionInfo, consumer));
    }

    @Override
    public void getObject(ObjectVersionId objectVersionId, Path path) {
        record("getObject", () -> inner.getObject(objectVersionId, path));
    }

    @Override
    public OcflObjectVersion getObject(ObjectVersionId objectVersionId) {
        return record("getObjectLazy", () -> inner.getObject(objectVersionId));
    }

    @Override
    public ObjectDetails describeObject(String objectId) {
        return record("describeObject", () -> inner.describeObject(objectId));
    }

    @Override
    public VersionDetails describeVersion(ObjectVersionId objectVersionId) {
        return record("describeVersion", () -> inner.describeVersion(objectVersionId));
    }

    @Override
    public FileChangeHistory fileChangeHistory(String objectId, String logicalPath) {
        return record("fileChangeHistory", () -> inner.fileChangeHistory(objectId, logicalPath));
    }

    @Override
    public boolean containsObject(String objectId) {
        return record("containsObject", () -> inner.containsObject(objectId));
    }

    @Override
    public Stream<String> listObjectIds() {
        return record("listObjectIds", inner::listObjectIds);
    }

    @Override
    public void purgeObject(String objectId) {
        record("purgeObject", () -> inner.purgeObject(objectId));
    }

    @Override
    public ValidationResults validateObject(String objectId, boolean contentFixityCheck) {
        return record("validateObject", () -> inner.validateObject(objectId, contentFixityCheck));
    }

    @Override
    public ObjectVersionId replicateVersionAsHead(ObjectVersionId objectVersionId, VersionInfo versionInfo) {
        return record("replicateVersionAsHead", () -> inner.replicateVersionAsHead(objectVersionId, versionInfo));
    }

    @Override
    public void rollbackToVersion(ObjectVersionId objectVersionId) {
        record("rollbackToVersion", () -> inner.rollbackToVersion(objectVersionId));
    }

    @Override
    public void exportVersion(ObjectVersionId objectVersionId, Path path, OcflOption... ocflOptions) {
        record("exportVersion", () -> inner.exportVersion(objectVersionId, path, ocflOptions));
    }

    @Override
    public void exportObject(String objectId, Path path, OcflOption... ocflOptions) {
        record("exportObject", () -> inner.exportObject(objectId, path, ocflOptions));
    }

    @Override
    public void importVersion(Path path, OcflOption... ocflOptions) {
        record("importVersion", () -> inner.importVersion(path, ocflOptions));
    }

    @Override
    public void importObject(Path path, OcflOption... ocflOptions) {
        record("importObject", () -> inner.importObject(path, ocflOptions));
    }

    @Override
    public void close() {
        record("close", inner::close);
    }

    @Override
    public OcflConfig config() {
        return record("config", inner::config);
    }

    @Override
    public void invalidateCache(String objectId) {
        record("invalidateCacheObject", () -> inner.invalidateCache(objectId));
    }

    @Override
    public void invalidateCache() {
        record("invalidateCacheAll", () -> inner.invalidateCache());
    }
}
