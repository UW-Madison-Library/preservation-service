package edu.wisc.library.sdg.preservation.worker.validation.lambda;

public class LambdaFunction {

    private final long maxSizeBytes;
    private final String name;

    public LambdaFunction(long maxSizeBytes, String name) {
        this.maxSizeBytes = maxSizeBytes;
        this.name = name;
    }

    public boolean accepts(long sizeBytes) {
        return sizeBytes <= maxSizeBytes;
    }

    public String getName() {
        return name;
    }
}
