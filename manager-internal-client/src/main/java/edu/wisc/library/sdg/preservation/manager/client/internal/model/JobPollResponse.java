/*
 * Preservation Manager Internal API
 * Internal API for the Preservation Manager.
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package edu.wisc.library.sdg.preservation.manager.client.internal.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CleanupSipsJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeleteDipJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeRestoreJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ProcessBatchJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ReplicateJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RestoreJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ValidateLocalJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ValidateRemoteJob;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * JobPollResponse
 */
@JsonPropertyOrder({
  JobPollResponse.JSON_PROPERTY_JOB_TYPE,
  JobPollResponse.JSON_PROPERTY_FINALIZE_RESTORE_JOB,
  JobPollResponse.JSON_PROPERTY_PROCESS_BATCH_JOB,
  JobPollResponse.JSON_PROPERTY_REPLICATE_JOB,
  JobPollResponse.JSON_PROPERTY_RESTORE_JOB,
  JobPollResponse.JSON_PROPERTY_RETRIEVE_JOB,
  JobPollResponse.JSON_PROPERTY_VALIDATE_LOCAL_JOB,
  JobPollResponse.JSON_PROPERTY_VALIDATE_REMOTE_JOB,
  JobPollResponse.JSON_PROPERTY_DELETE_DIP_JOB,
  JobPollResponse.JSON_PROPERTY_CLEANUP_SIPS_JOB
})
@JsonTypeName("JobPollResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class JobPollResponse {
  public static final String JSON_PROPERTY_JOB_TYPE = "jobType";
  private JobType jobType;

  public static final String JSON_PROPERTY_FINALIZE_RESTORE_JOB = "finalizeRestoreJob";
  private FinalizeRestoreJob finalizeRestoreJob;

  public static final String JSON_PROPERTY_PROCESS_BATCH_JOB = "processBatchJob";
  private ProcessBatchJob processBatchJob;

  public static final String JSON_PROPERTY_REPLICATE_JOB = "replicateJob";
  private ReplicateJob replicateJob;

  public static final String JSON_PROPERTY_RESTORE_JOB = "restoreJob";
  private RestoreJob restoreJob;

  public static final String JSON_PROPERTY_RETRIEVE_JOB = "retrieveJob";
  private RetrieveJob retrieveJob;

  public static final String JSON_PROPERTY_VALIDATE_LOCAL_JOB = "validateLocalJob";
  private ValidateLocalJob validateLocalJob;

  public static final String JSON_PROPERTY_VALIDATE_REMOTE_JOB = "validateRemoteJob";
  private ValidateRemoteJob validateRemoteJob;

  public static final String JSON_PROPERTY_DELETE_DIP_JOB = "deleteDipJob";
  private DeleteDipJob deleteDipJob;

  public static final String JSON_PROPERTY_CLEANUP_SIPS_JOB = "cleanupSipsJob";
  private CleanupSipsJob cleanupSipsJob;

  public JobPollResponse() { 
  }

  public JobPollResponse jobType(JobType jobType) {
    
    this.jobType = jobType;
    return this;
  }

   /**
   * Get jobType
   * @return jobType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_JOB_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public JobType getJobType() {
    return jobType;
  }


  @JsonProperty(JSON_PROPERTY_JOB_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setJobType(JobType jobType) {
    this.jobType = jobType;
  }


  public JobPollResponse finalizeRestoreJob(FinalizeRestoreJob finalizeRestoreJob) {
    
    this.finalizeRestoreJob = finalizeRestoreJob;
    return this;
  }

   /**
   * Get finalizeRestoreJob
   * @return finalizeRestoreJob
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_FINALIZE_RESTORE_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public FinalizeRestoreJob getFinalizeRestoreJob() {
    return finalizeRestoreJob;
  }


  @JsonProperty(JSON_PROPERTY_FINALIZE_RESTORE_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFinalizeRestoreJob(FinalizeRestoreJob finalizeRestoreJob) {
    this.finalizeRestoreJob = finalizeRestoreJob;
  }


  public JobPollResponse processBatchJob(ProcessBatchJob processBatchJob) {
    
    this.processBatchJob = processBatchJob;
    return this;
  }

   /**
   * Get processBatchJob
   * @return processBatchJob
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_PROCESS_BATCH_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ProcessBatchJob getProcessBatchJob() {
    return processBatchJob;
  }


  @JsonProperty(JSON_PROPERTY_PROCESS_BATCH_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setProcessBatchJob(ProcessBatchJob processBatchJob) {
    this.processBatchJob = processBatchJob;
  }


  public JobPollResponse replicateJob(ReplicateJob replicateJob) {
    
    this.replicateJob = replicateJob;
    return this;
  }

   /**
   * Get replicateJob
   * @return replicateJob
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_REPLICATE_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ReplicateJob getReplicateJob() {
    return replicateJob;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATE_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicateJob(ReplicateJob replicateJob) {
    this.replicateJob = replicateJob;
  }


  public JobPollResponse restoreJob(RestoreJob restoreJob) {
    
    this.restoreJob = restoreJob;
    return this;
  }

   /**
   * Get restoreJob
   * @return restoreJob
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_RESTORE_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public RestoreJob getRestoreJob() {
    return restoreJob;
  }


  @JsonProperty(JSON_PROPERTY_RESTORE_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRestoreJob(RestoreJob restoreJob) {
    this.restoreJob = restoreJob;
  }


  public JobPollResponse retrieveJob(RetrieveJob retrieveJob) {
    
    this.retrieveJob = retrieveJob;
    return this;
  }

   /**
   * Get retrieveJob
   * @return retrieveJob
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_RETRIEVE_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public RetrieveJob getRetrieveJob() {
    return retrieveJob;
  }


  @JsonProperty(JSON_PROPERTY_RETRIEVE_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRetrieveJob(RetrieveJob retrieveJob) {
    this.retrieveJob = retrieveJob;
  }


  public JobPollResponse validateLocalJob(ValidateLocalJob validateLocalJob) {
    
    this.validateLocalJob = validateLocalJob;
    return this;
  }

   /**
   * Get validateLocalJob
   * @return validateLocalJob
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_VALIDATE_LOCAL_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ValidateLocalJob getValidateLocalJob() {
    return validateLocalJob;
  }


  @JsonProperty(JSON_PROPERTY_VALIDATE_LOCAL_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setValidateLocalJob(ValidateLocalJob validateLocalJob) {
    this.validateLocalJob = validateLocalJob;
  }


  public JobPollResponse validateRemoteJob(ValidateRemoteJob validateRemoteJob) {
    
    this.validateRemoteJob = validateRemoteJob;
    return this;
  }

   /**
   * Get validateRemoteJob
   * @return validateRemoteJob
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_VALIDATE_REMOTE_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ValidateRemoteJob getValidateRemoteJob() {
    return validateRemoteJob;
  }


  @JsonProperty(JSON_PROPERTY_VALIDATE_REMOTE_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setValidateRemoteJob(ValidateRemoteJob validateRemoteJob) {
    this.validateRemoteJob = validateRemoteJob;
  }


  public JobPollResponse deleteDipJob(DeleteDipJob deleteDipJob) {
    
    this.deleteDipJob = deleteDipJob;
    return this;
  }

   /**
   * Get deleteDipJob
   * @return deleteDipJob
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_DELETE_DIP_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public DeleteDipJob getDeleteDipJob() {
    return deleteDipJob;
  }


  @JsonProperty(JSON_PROPERTY_DELETE_DIP_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDeleteDipJob(DeleteDipJob deleteDipJob) {
    this.deleteDipJob = deleteDipJob;
  }


  public JobPollResponse cleanupSipsJob(CleanupSipsJob cleanupSipsJob) {
    
    this.cleanupSipsJob = cleanupSipsJob;
    return this;
  }

   /**
   * Get cleanupSipsJob
   * @return cleanupSipsJob
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_CLEANUP_SIPS_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public CleanupSipsJob getCleanupSipsJob() {
    return cleanupSipsJob;
  }


  @JsonProperty(JSON_PROPERTY_CLEANUP_SIPS_JOB)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCleanupSipsJob(CleanupSipsJob cleanupSipsJob) {
    this.cleanupSipsJob = cleanupSipsJob;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JobPollResponse jobPollResponse = (JobPollResponse) o;
    return Objects.equals(this.jobType, jobPollResponse.jobType) &&
        Objects.equals(this.finalizeRestoreJob, jobPollResponse.finalizeRestoreJob) &&
        Objects.equals(this.processBatchJob, jobPollResponse.processBatchJob) &&
        Objects.equals(this.replicateJob, jobPollResponse.replicateJob) &&
        Objects.equals(this.restoreJob, jobPollResponse.restoreJob) &&
        Objects.equals(this.retrieveJob, jobPollResponse.retrieveJob) &&
        Objects.equals(this.validateLocalJob, jobPollResponse.validateLocalJob) &&
        Objects.equals(this.validateRemoteJob, jobPollResponse.validateRemoteJob) &&
        Objects.equals(this.deleteDipJob, jobPollResponse.deleteDipJob) &&
        Objects.equals(this.cleanupSipsJob, jobPollResponse.cleanupSipsJob);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobType, finalizeRestoreJob, processBatchJob, replicateJob, restoreJob, retrieveJob, validateLocalJob, validateRemoteJob, deleteDipJob, cleanupSipsJob);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JobPollResponse {\n");
    sb.append("    jobType: ").append(toIndentedString(jobType)).append("\n");
    sb.append("    finalizeRestoreJob: ").append(toIndentedString(finalizeRestoreJob)).append("\n");
    sb.append("    processBatchJob: ").append(toIndentedString(processBatchJob)).append("\n");
    sb.append("    replicateJob: ").append(toIndentedString(replicateJob)).append("\n");
    sb.append("    restoreJob: ").append(toIndentedString(restoreJob)).append("\n");
    sb.append("    retrieveJob: ").append(toIndentedString(retrieveJob)).append("\n");
    sb.append("    validateLocalJob: ").append(toIndentedString(validateLocalJob)).append("\n");
    sb.append("    validateRemoteJob: ").append(toIndentedString(validateRemoteJob)).append("\n");
    sb.append("    deleteDipJob: ").append(toIndentedString(deleteDipJob)).append("\n");
    sb.append("    cleanupSipsJob: ").append(toIndentedString(cleanupSipsJob)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

