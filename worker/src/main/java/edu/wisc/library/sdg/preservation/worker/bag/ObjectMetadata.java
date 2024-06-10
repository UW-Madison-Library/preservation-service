package edu.wisc.library.sdg.preservation.worker.bag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ObjectMetadata {

    private final String objectId;
    private final Map<String, FileMetadata> fileMap;

    public ObjectMetadata(String objectId) {
        this.objectId = objectId;
        this.fileMap = new HashMap<>();
    }

    public void addFile(FileMetadata fileMetadata) {
        fileMap.put(fileMetadata.getFilePath(), fileMetadata);
    }

    /**
     * Gets the metadata related to the specified file. The file path should be relative the object root.
     *
     * @param file object root relative path to the file
     * @return related metadata or null
     */
    public FileMetadata getFileMeta(String file) {
        return fileMap.get(file);
    }

    public Collection<FileMetadata> getAllFileMeta() {
        return fileMap.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectMetadata that = (ObjectMetadata) o;
        return Objects.equals(objectId, that.objectId) && Objects.equals(fileMap, that.fileMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, fileMap);
    }

    @Override
    public String toString() {
        return "ObjectMetadata{" +
                "objectId='" + objectId + '\'' +
                ", fileMap=" + fileMap +
                '}';
    }
}
