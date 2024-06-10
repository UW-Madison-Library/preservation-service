package edu.wisc.library.sdg.preservation.worker.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.PreservationPaths;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectFile;
import edu.wisc.library.sdg.preservation.worker.storage.remote.RemoteDataStore;
import edu.wisc.library.sdg.preservation.worker.validation.lambda.GlacierValidationRequest;
import edu.wisc.library.sdg.preservation.worker.validation.lambda.GlacierValidationResponse;
import edu.wisc.library.sdg.preservation.worker.validation.lambda.LambdaFunction;
import edu.wisc.library.sdg.preservation.worker.validation.lambda.Validity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.LogType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class DefaultGlacierVersionValidator implements GlacierVersionValidator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultGlacierVersionValidator.class);

    private final RemoteDataStore glacierDataStore;
    private final LambdaClient lambdaClient;
    private final ObjectMapper objectMapper;
    private final String bucket;
    private final List<LambdaFunction> functions;

    public DefaultGlacierVersionValidator(RemoteDataStore glacierDataStore,
                                          LambdaClient lambdaClient,
                                          String bucket,
                                          List<LambdaFunction> functions) {
        this.glacierDataStore = glacierDataStore;
        this.lambdaClient = lambdaClient;
        this.bucket = bucket;
        this.functions = functions;
        this.objectMapper = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
    }

    @Override
    public void validate(String objectId,
                         String persistenceVersion,
                         String key,
                         String sha256Digest,
                         List<ObjectFile> expectedFiles) {
        LOG.info("Validating {} in Glacier", key);

        var function = identifyFunction(key);

        var request = new GlacierValidationRequest()
                .setBucket(bucket)
                .setKey(key)
                .setSha256Digest(sha256Digest)
                .setContentPath(PreservationPaths.archiveObjectContent(objectId, persistenceVersion));

        expectedFiles.forEach(file -> {
            request.addFile(file.getFilePath(), file.getSha256Digest());
        });

        var requestBytes = serialize(request);

        var lambdaResponse = lambdaClient.invoke(lambda -> {
            lambda.functionName(function.getName())
                    .logType(LogType.TAIL)
                    .payload(SdkBytes.fromByteArray(requestBytes));
        });

        var validationResponse = deserialize(lambdaResponse.payload().asByteArray());

        LOG.info("Glacier validation result: {}", validationResponse);

        if (validationResponse.getValidity() != Validity.VALID) {
            throw new ValidationException(validationResponse.getReason());
        }
    }

    private LambdaFunction identifyFunction(String key) {
        var size = glacierDataStore.fileSize(key);

        for (var func : functions) {
            if (func.accepts(size)) {
                return func;
            }
        }

        throw new RuntimeException(String.format("No suitable Lambda function found for %s of %s bytes", key, size));
    }

    private byte[] serialize(GlacierValidationRequest request) {
        try {
            return objectMapper.writeValueAsBytes(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private GlacierValidationResponse deserialize(byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, GlacierValidationResponse.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
