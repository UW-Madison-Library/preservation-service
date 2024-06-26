/*
 * Preservation Manager Public API
 * Public API for the Preservation Manager.
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package edu.wisc.library.sdg.preservation.manager.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import edu.wisc.library.sdg.preservation.manager.client.model.JobDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * ListJobsResponse
 */
@JsonPropertyOrder({
  ListJobsResponse.JSON_PROPERTY_JOBS
})
@JsonTypeName("ListJobsResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class ListJobsResponse {
  public static final String JSON_PROPERTY_JOBS = "jobs";
  private List<JobDetails> jobs = new ArrayList<>();

  public ListJobsResponse() { 
  }

  public ListJobsResponse jobs(List<JobDetails> jobs) {
    
    this.jobs = jobs;
    return this;
  }

  public ListJobsResponse addJobsItem(JobDetails jobsItem) {
    this.jobs.add(jobsItem);
    return this;
  }

   /**
   * List of jobs
   * @return jobs
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "List of jobs")
  @JsonProperty(JSON_PROPERTY_JOBS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<JobDetails> getJobs() {
    return jobs;
  }


  @JsonProperty(JSON_PROPERTY_JOBS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setJobs(List<JobDetails> jobs) {
    this.jobs = jobs;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListJobsResponse listJobsResponse = (ListJobsResponse) o;
    return Objects.equals(this.jobs, listJobsResponse.jobs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListJobsResponse {\n");
    sb.append("    jobs: ").append(toIndentedString(jobs)).append("\n");
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

