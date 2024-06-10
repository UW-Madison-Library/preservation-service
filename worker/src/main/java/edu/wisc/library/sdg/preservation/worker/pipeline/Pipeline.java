package edu.wisc.library.sdg.preservation.worker.pipeline;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a series of sequential steps that are called one after the other and operate on a shared context object.
 */
public class Pipeline<T extends PipelineContext> {

    private final Iterable<PipelineStep<T>> steps;

    public static <T extends PipelineContext> Builder<T> builder() {
        return new Builder<>();
    }

    private Pipeline(Iterable<PipelineStep<T>> steps) {
        this.steps = ArgCheck.notNull(steps, "steps");
    }

    /**
     * Start executing the series of steps on the given context.
     */
    public void execute(T context) {
        ArgCheck.notNull(context, "context");
        new NextStep<>(context, steps).execute();
    }

    public static class Builder<T extends PipelineContext> {

        private final List<PipelineStep<T>> steps;

        public Builder() {
            this.steps = new ArrayList<>();
        }

        public Builder<T> addStep(PipelineStep<T> step) {
            ArgCheck.notNull(step, "step");
            steps.add(step);
            return this;
        }

        public Pipeline<T> build() {
            return new Pipeline<>(steps);
        }

    }

}
