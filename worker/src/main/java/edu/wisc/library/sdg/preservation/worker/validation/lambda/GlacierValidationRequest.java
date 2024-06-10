package edu.wisc.library.sdg.preservation.worker.validation.lambda;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;

import java.util.HashMap;
import java.util.Map;

public class GlacierValidationRequest {

    private String bucket;
    private String key;
    private String sha256Digest;
    private String contentPath;
    private Map<String, String> files;

    public GlacierValidationRequest() {
        this.files = new HashMap<>();
    }

    public String getBucket() {
        return bucket;
    }

    public GlacierValidationRequest setBucket(String bucket) {
        this.bucket = ArgCheck.notBlank(bucket, "bucket");
        return this;
    }

    public String getKey() {
        return key;
    }

    public GlacierValidationRequest setKey(String key) {
        this.key = ArgCheck.notBlank(key, "key");
        return this;
    }

    public String getSha256Digest() {
        return sha256Digest;
    }

    public GlacierValidationRequest setSha256Digest(String sha256Digest) {
        this.sha256Digest = ArgCheck.notBlank(sha256Digest, "sha256Digest");
        return this;
    }

    public String getContentPath() {
        return contentPath;
    }

    public GlacierValidationRequest setContentPath(String contentPath) {
        this.contentPath = contentPath;
        return this;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public GlacierValidationRequest setFiles(Map<String, String> files) {
        this.files = ArgCheck.notNull(files, "files");
        return this;
    }

    public GlacierValidationRequest addFile(String file, String digest) {
        ArgCheck.notBlank(file, "file");
        ArgCheck.notBlank(digest, "digest");
        files.put(file, digest);
        return this;
    }

    @Override
    public String toString() {
        return "GlacierValidationRequest{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", sha256Digest='" + sha256Digest + '\'' +
                ", contentPath='" + contentPath + '\'' +
                ", files=" + files +
                '}';
    }
}
