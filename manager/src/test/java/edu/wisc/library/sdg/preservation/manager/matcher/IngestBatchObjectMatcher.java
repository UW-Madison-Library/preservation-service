package edu.wisc.library.sdg.preservation.manager.matcher;

import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchObject;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

public class IngestBatchObjectMatcher extends TypeSafeMatcher<IngestBatchObject> {

    private IngestBatchObject expected;

    public IngestBatchObjectMatcher(IngestBatchObject expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(IngestBatchObject item) {
        return Objects.equals(expected.getIngestId(), item.getIngestId())
                && Objects.equals(expected.getExternalObjectId(), item.getExternalObjectId())
                && Objects.equals(expected.getState(), item.getState())
                && Objects.equals(expected.getReviewedBy(), item.getReviewedBy());
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }

}
