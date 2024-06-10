package edu.wisc.library.sdg.preservation.manager.matcher;

import edu.wisc.library.sdg.preservation.manager.client.model.ObjectFile;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

import static org.hamcrest.Matchers.containsInAnyOrder;

public class ObjectFileMatcher extends TypeSafeMatcher<ObjectFile> {

    private ObjectFile expected;

    public ObjectFileMatcher(ObjectFile expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(ObjectFile item) {
        return Objects.equals(expected.getFilePath(), item.getFilePath())
                && Objects.equals(expected.getSha256Digest(), item.getSha256Digest())
                && Objects.equals(expected.getFileSize(), item.getFileSize())
                && (expected.getFormats() == null || containsInAnyOrder(expected.getFormats().toArray()).matches(item.getFormats()));
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }

}
