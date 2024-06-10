package edu.wisc.library.sdg.preservation.worker.storage.remote;

import at.favre.lib.bytes.Bytes;
import com.google.common.hash.Hashing;
import com.google.common.io.MoreFiles;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.InvalidObjectStateException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.ObjectLockLegalHoldStatus;
import software.amazon.awssdk.services.s3.model.StorageClass;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.Tagging;
import software.amazon.awssdk.services.s3.model.Tier;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

public class GlacierRemoteDataStore implements RemoteDataStore {

    private static final Logger LOG = LoggerFactory.getLogger(GlacierRemoteDataStore.class);

    private static final String STORE = "Glacier";

    private static final int KB = 1024;
    private static final int MB = 1024 * KB;
    private static final long GB = 1024 * MB;
    private static final long TB = 1024 * GB;

    private static final long MAX_FILE_BYTES = 5 * TB;

    private static final int MAX_PART_BYTES = 100 * MB;
    private static final int PART_SIZE_BYTES = 10 * MB;

    private static final int MAX_PARTS = 100;
    private static final int PART_SIZE_INCREMENT = 10;
    private static final int PARTS_INCREMENT = 100;

    private static final String CONTENT_TYPE = "application/zip";

    private static final String RESTORE_IN_PROGRESS = "ongoing-request=\"true\"";
    private static final Pattern EXTRACT_EXPIRY = Pattern.compile(".*expiry-date=\"([^\"]+)\".*");

    // Number of days a restored object should remain in S3
    private static final int DAYS = 1;

    private final S3Client s3Client;
    private final String bucket;

    public GlacierRemoteDataStore(S3Client s3Client, String bucket) {
        this.s3Client = s3Client;
        this.bucket = bucket;
    }

    @Override
    @Timed(value = "remote", extraTags = {"store", STORE})
    public String writeObjectVersion(String objectId,
                                     String persistenceVersion,
                                     Path archive) {
        ArgCheck.notBlank(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");
        ArgCheck.notNull(archive, "archive");

        var key = constructKey(objectId, persistenceVersion);

        LOG.info("Writing object <{}, {}> to S3-Glacier <{}> at <{}>",
                objectId, persistenceVersion, bucket, key);

        uploadFile(objectId, persistenceVersion, key, archive);

        return key;
    }

    @Override
    @Timed(value = "remote", extraTags = {"store", STORE})
    public boolean download(String key, Path destination) {
        ArgCheck.notBlank(key, "key");
        ArgCheck.notNull(destination, "destination");

        try {
            s3Client.getObject(request -> {
                request.bucket(bucket).key(key);
            }, destination);
            return true;
        } catch (InvalidObjectStateException e) {
            LOG.debug("Object {} is not ready for download", key);
            initiateObjectVersionRestoration(key);
            return false;
        }
    }

    @Override
    @Timed(value = "remote", extraTags = {"store", STORE})
    public boolean containsObjectVersion(String objectId, String persistenceVersion) {
        ArgCheck.notBlank(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");

        var key = constructKey(objectId, persistenceVersion);

        LOG.debug("Looking for <{}, {}> in S3-Glacier <{}> at <{}>",
                objectId, persistenceVersion, bucket, key);

        try {
            s3Client.headObject(request -> {
                request.bucket(bucket).key(key);
            });
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    @Timed(value = "remote", extraTags = {"store", STORE})
    public boolean initiateObjectVersionRestoration(String key) {
        ArgCheck.notNull(key, "key");

        var availability = getAvailability(key);

        if (availability == Availability.AVAILABLE) {
            return true;
        } else if (availability == Availability.RESTORING) {
            return false;
        }

        LOG.info("Submitting object restore request for <{}>", key);
        s3Client.restoreObject(builder -> {
            builder.bucket(bucket).key(key)
                    .restoreRequest(req -> req.days(DAYS).glacierJobParameters(param -> param.tier(Tier.BULK)));
        });

        return false;
    }

    @Override
    @Timed(value = "remote", extraTags = {"store", STORE})
    public long fileSize(String key) {
        var response = s3Client.headObject(request -> {
            request.bucket(bucket).key(key);
        });
        return response.contentLength();
    }

    private String constructKey(String objectId, String persistenceVersion) {
        var unprefixedId = UuidUtils.withoutPrefix(objectId);
        return String.format("%s/%s-%s.zip", unprefixedId, unprefixedId, persistenceVersion);
    }

    private Availability getAvailability(String key) {
        var head = s3Client.headObject(builder -> {
            builder.bucket(bucket).key(key);
        });

        // This should be something like: ongoing-request="false", expiry-date="Sun, 23 Dec 2012 00:00:00 GMT"
        // OR: ongoing-request="true"
        // Will be null if the object has never had a restore request, regardless of its state
        var restoreStatus = head.restore();
        var storageClass = head.storageClass();

        LOG.debug("Object <{}> restore status <{}> and is in storage class <{}>", key, restoreStatus, storageClass);

        if (RESTORE_IN_PROGRESS.equals(restoreStatus)) {
            LOG.info("Object <{}> is already in the process of being restored", restoreStatus);
            return Availability.RESTORING;
        } else if (restoreStatus != null) {
            var matcher = EXTRACT_EXPIRY.matcher(restoreStatus);
            if (matcher.matches()) {
                var timestampStr = matcher.group(1);
                var timestamp = OffsetDateTime.parse(timestampStr, DateTimeFormatter.RFC_1123_DATE_TIME);
                LOG.debug("Object <{}> was restored and will expire at {}", key, timestampStr);

                if (timestamp.toInstant().isAfter(Instant.now())) {
                    return Availability.AVAILABLE;
                }
            }
        } else if (storageClass == null || storageClass == StorageClass.STANDARD) {
            return Availability.AVAILABLE;
        }

        return Availability.UNAVAILABLE;
    }

    private void uploadFile(String objectId, String persistenceVersion, String key, Path file) {
        var fileSize = UncheckedFiles.size(file);

        if (fileSize >= MAX_FILE_BYTES) {
            throw new RuntimeException(
                    String.format("Cannot store file %s to S3-Glacier because it exceeds the maximum file size.", file));
        }

        if (fileSize > MAX_PART_BYTES) {
            multipartUpload(objectId, persistenceVersion, key, file, fileSize);
        } else {
            LOG.debug("Uploading {} to bucket {} key {} size {}", file, bucket, key, fileSize);

            var md5Sum = md5Sum(file);

            s3Client.putObject(request -> {
                request.bucket(bucket)
                        .key(key)
                        .contentType(CONTENT_TYPE)
                        .objectLockLegalHoldStatus(ObjectLockLegalHoldStatus.ON)
                        .contentMD5(md5Sum)
                        .tagging(Tagging.builder()
                                .tagSet(
                                        Tag.builder().key("objectIdBase64")
                                                // MUST base64 encode because tags only support: + - = . _ : / @
                                                .value(Base64.getEncoder().encodeToString(objectId.getBytes(StandardCharsets.UTF_8))).build(),
                                        Tag.builder().key("version").value(persistenceVersion).build()
                                        // TODO other tags?
                                )
                                .build());
            }, file);
        }
    }

    private void multipartUpload(String objectId,
                                 String persistenceVersion,
                                 String key,
                                 Path file,
                                 long fileSize) {
        var partSize = determinePartSize(fileSize);

        LOG.debug("Multipart upload of {} to bucket {} key {}. File size: {}; part size: {}", file, bucket, key,
                fileSize, partSize);

        var uploadId = beginMultipartUpload(objectId, persistenceVersion, key);

        var completedParts = new ArrayList<CompletedPart>();

        try {
            try (var channel = FileChannel.open(file, StandardOpenOption.READ)) {
                var buffer = ByteBuffer.allocate(partSize);
                var i = 1;

                while (channel.read(buffer) > 0) {
                    buffer.flip();

                    var md5Sum = md5Sum(buffer);

                    var partResponse = s3Client.uploadPart(UploadPartRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .uploadId(uploadId)
                            .partNumber(i)
                            .contentMD5(md5Sum)
                            .build(), RequestBody.fromByteBuffer(buffer));

                    completedParts.add(CompletedPart.builder()
                            .partNumber(i)
                            .eTag(partResponse.eTag())
                            .build());

                    buffer.clear();
                    i++;
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            completeMultipartUpload(uploadId, key, completedParts);
        } catch (RuntimeException e) {
            abortMultipartUpload(uploadId, key);
            throw e;
        }
    }

    private String beginMultipartUpload(String objectId, String persistenceVersion, String key) {
        return s3Client.createMultipartUpload(CreateMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(CONTENT_TYPE)
                .objectLockLegalHoldStatus(ObjectLockLegalHoldStatus.ON)
                .tagging(Tagging.builder()
                        .tagSet(
                                Tag.builder().key("objectId").value(objectId).build(),
                                Tag.builder().key("version").value(persistenceVersion).build()
                                // TODO other tags?
                        )
                        .build())
                .build()).uploadId();
    }

    private void completeMultipartUpload(String uploadId, String key, List<CompletedPart> parts) {
        s3Client.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(key)
                .uploadId(uploadId)
                .multipartUpload(CompletedMultipartUpload.builder()
                        .parts(parts)
                        .build())
                .build());
    }

    private void abortMultipartUpload(String uploadId, String key) {
        try {
            s3Client.abortMultipartUpload(AbortMultipartUploadRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .uploadId(uploadId)
                    .build());
        } catch (RuntimeException e) {
            LOG.error("Failed to abort multipart upload. Bucket: {}; Key: {}; Upload Id: {}", bucket, key, uploadId, e);
        }
    }

    private int determinePartSize(long fileSize) {
        var partSize = PART_SIZE_BYTES;
        var maxParts = MAX_PARTS;

        while (fileSize / partSize > maxParts) {
            partSize += PART_SIZE_INCREMENT;

            if (partSize > MAX_PART_BYTES) {
                maxParts += PARTS_INCREMENT;
                partSize /= 2;
            }
        }

        return partSize;
    }

    private String md5Sum(Path path) {
        try {
            return Bytes.from(MoreFiles.asByteSource(path).hash(Hashing.md5()).asBytes()).encodeBase64();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String md5Sum(ByteBuffer buffer) {
        var digest = md5Digest();
        digest.update(buffer);
        buffer.flip();
        return Bytes.from(digest.digest()).encodeBase64();
    }

    private MessageDigest md5Digest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static enum Availability {
        AVAILABLE,
        UNAVAILABLE,
        RESTORING
    }

}
