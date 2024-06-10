package edu.wisc.library.sdg.preservation.worker.validation;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectFile;
import edu.wisc.library.sdg.preservation.worker.storage.remote.GlacierRemoteDataStore;
import edu.wisc.library.sdg.preservation.worker.validation.lambda.LambdaFunction;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;

// It's not possible to meaningfully test this class without a live connection to lambda
@Disabled
public class GlacierVersionValidatorTest {

    @Test
    public void test() {
        var secret = "";
        var access = "";
        var bucket = "";

        var credsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(
                secret, access));
        var s3Client = S3Client.builder()
                .credentialsProvider(credsProvider)
                .region(Region.US_EAST_2)
                .httpClientBuilder(ApacheHttpClient.builder())
                .build();
        var lambdaClient = LambdaClient.builder()
                .credentialsProvider(credsProvider)
                .region(Region.US_EAST_2)
                .httpClientBuilder(ApacheHttpClient.builder())
                .build();
        var glacier = new GlacierRemoteDataStore(s3Client, bucket);

        var validator = new DefaultGlacierVersionValidator(glacier, lambdaClient, bucket, List.of(
                new LambdaFunction(20 * 1024 * 1024, "validate-rb")
        ));

        validator.validate("test", "v1", "vault/test-v1.zip",
                "0678f6f4a17fb5727203ca3e4d048339819dd97529b3818b46f5c957d99a840e",
                List.of(
                        new ObjectFile().filePath("ADMINMD.xml").sha256Digest("d59ad8702fd9613ae2737630dd95f8657aac0047ededde09a1e6c63c3002a372"),
                        new ObjectFile().filePath("DC.xml").sha256Digest("5c6261398bb5ede5fe88f0f7a4ffa98d918c59907cd11d6a7ec605f48d505545"),
                        new ObjectFile().filePath("MASTER0.tif").sha256Digest("2163f817760f782c7254c07e1b0e5f43f629d1c1fa45640656500fdd4211b27a"),
                        new ObjectFile().filePath("RELS-EXT.xml").sha256Digest("0f420c0e6a5dfac8c0946c6d673920d092773ecbbf8cda8dcad68d4cc41cbf0e"),
                        new ObjectFile().filePath("RELS-INT.xml").sha256Digest("1deaf21a0787a2962481dc065ec9d5873be12cdda08f088451afdbbae8b87940"),
                        new ObjectFile().filePath("TECH0.xml").sha256Digest("121d6a38e13b078eb5d86813ac7a2fc185bbe073a01f7932091faf568b868398"),
                        new ObjectFile().filePath("TECH1.xml").sha256Digest("e5942145199751e08db450248e8a7fe8f47fab8e336e497f5c1761cb2695d25a"),
                        new ObjectFile().filePath("TEXT0.txt").sha256Digest("9bc61a954dac0808f2f1fb710a6e3945202a4f4814d0855dc0a342b729a2ce6a")
                ));
    }

}
