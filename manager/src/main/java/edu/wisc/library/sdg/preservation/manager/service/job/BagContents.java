package edu.wisc.library.sdg.preservation.manager.service.job;

import java.util.ArrayList;
import java.util.List;

public class BagContents {

    private long remainingSpace;
    private final long cutoff;
    private final List<ObjectVersionDetails> contents;

    public BagContents(long maxSizeBytes) {
        remainingSpace = maxSizeBytes;
        cutoff = Math.round(maxSizeBytes * 0.05);
        contents = new ArrayList<>();
    }

    public boolean add(ObjectDetails object) {
        if (object.size() <= remainingSpace) {
            contents.addAll(object.getVersions());
            remainingSpace -= object.size();
            return true;
        }
        return false;
    }

    public void forceAdd(ObjectDetails object) {
        contents.addAll(object.getVersions());
        remainingSpace -= object.size();
    }

    public boolean isFull() {
        return remainingSpace < cutoff;
    }

    public List<ObjectVersionDetails> getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "BagContents{" +
                "remainingSpace=" + remainingSpace +
                ", contents=" + contents +
                '}';
    }

}
