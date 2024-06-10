package edu.wisc.library.sdg.preservation.worker.pipeline;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Encapsulates the next step in a chain of steps. Call execute() to continue to execute the chain.
 */
public class NextStep<T extends PipelineContext> {

    private final T context;
    private final Iterable<PipelineStep<T>> steps;
    private final Iterator<PipelineStep<T>> currentStep;

    private boolean executed = false;

    public NextStep(T context, Iterable<PipelineStep<T>> steps) {
        this(context, steps, steps.iterator());
    }

    private NextStep(NextStep<T> originalNextStep) {
        this(originalNextStep.context, originalNextStep.steps, originalNextStep.currentStep);
    }

    private NextStep(T context, Iterable<PipelineStep<T>> steps, Iterator<PipelineStep<T>> currentStep) {
        this.context = checkNotNull(context, "context cannot be null");
        this.steps = checkNotNull(steps, "steps cannot be null");
        this.currentStep = checkNotNull(currentStep, "currentStep cannot be null");
    }

    /**
     * Executes the next step in a chain of steps.
     */
    public void execute() {
        if (executed) {
            throw new IllegalStateException("NextStep has already been executed once for the current step.");
        }

        executed = true;

        if (currentStep.hasNext()) {
            currentStep.next().execute(context, new NextStep<>(this));
        }
    }

}
