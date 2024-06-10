package edu.wisc.library.sdg.preservation.manager.matcher;

import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchObject;
import edu.wisc.library.sdg.preservation.manager.client.model.ObjectFile;

import java.util.function.Consumer;

public final class PreservationMatchers {

    private PreservationMatchers() {

    }

    public static ObjectFileMatcher objectFile(Consumer<ObjectFile> consumer) {
        var expected = new ObjectFile();
        consumer.accept(expected);
        return new ObjectFileMatcher(expected);
    }

    public static IngestBatchObjectMatcher ingestBatchObject(Consumer<IngestBatchObject> consumer) {
        var expected = new IngestBatchObject();
        consumer.accept(expected);
        return new IngestBatchObjectMatcher(expected);
    }

}
