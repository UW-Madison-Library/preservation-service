package edu.wisc.library.sdg.preservation.worker.pipeline;

/**
 * Interface for a step in a pipeline
 */
public interface PipelineStep<T extends PipelineContext> {

    void execute(T context, NextStep<T> nextStep);

}
