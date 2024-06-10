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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * RetrieveRequestPart
 */
@JsonPropertyOrder({
  RetrieveRequestPart.JSON_PROPERTY_JOB_ID,
  RetrieveRequestPart.JSON_PROPERTY_STATE,
  RetrieveRequestPart.JSON_PROPERTY_LAST_DOWNLOADED_TIMESTAMP
})
@JsonTypeName("RetrieveRequestPart")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class RetrieveRequestPart {
  public static final String JSON_PROPERTY_JOB_ID = "jobId";
  private Long jobId;

  /**
   * Indicates the state of this request part
   */
  public enum StateEnum {
    READY("READY"),
    
    NOT_READY("NOT_READY"),
    
    FAILED("FAILED"),
    
    DELETED("DELETED"),
    
    CANCELLED("CANCELLED");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StateEnum fromValue(String value) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_STATE = "state";
  private StateEnum state;

  public static final String JSON_PROPERTY_LAST_DOWNLOADED_TIMESTAMP = "lastDownloadedTimestamp";
  private OffsetDateTime lastDownloadedTimestamp;

  public RetrieveRequestPart() { 
  }

  public RetrieveRequestPart jobId(Long jobId) {
    
    this.jobId = jobId;
    return this;
  }

   /**
   * The id of a job
   * @return jobId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10293", value = "The id of a job")
  @JsonProperty(JSON_PROPERTY_JOB_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getJobId() {
    return jobId;
  }


  @JsonProperty(JSON_PROPERTY_JOB_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setJobId(Long jobId) {
    this.jobId = jobId;
  }


  public RetrieveRequestPart state(StateEnum state) {
    
    this.state = state;
    return this;
  }

   /**
   * Indicates the state of this request part
   * @return state
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Indicates the state of this request part")
  @JsonProperty(JSON_PROPERTY_STATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public StateEnum getState() {
    return state;
  }


  @JsonProperty(JSON_PROPERTY_STATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setState(StateEnum state) {
    this.state = state;
  }


  public RetrieveRequestPart lastDownloadedTimestamp(OffsetDateTime lastDownloadedTimestamp) {
    
    this.lastDownloadedTimestamp = lastDownloadedTimestamp;
    return this;
  }

   /**
   * Timestamp this part was last downloaded
   * @return lastDownloadedTimestamp
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Timestamp this part was last downloaded")
  @JsonProperty(JSON_PROPERTY_LAST_DOWNLOADED_TIMESTAMP)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public OffsetDateTime getLastDownloadedTimestamp() {
    return lastDownloadedTimestamp;
  }


  @JsonProperty(JSON_PROPERTY_LAST_DOWNLOADED_TIMESTAMP)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setLastDownloadedTimestamp(OffsetDateTime lastDownloadedTimestamp) {
    this.lastDownloadedTimestamp = lastDownloadedTimestamp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RetrieveRequestPart retrieveRequestPart = (RetrieveRequestPart) o;
    return Objects.equals(this.jobId, retrieveRequestPart.jobId) &&
        Objects.equals(this.state, retrieveRequestPart.state) &&
        Objects.equals(this.lastDownloadedTimestamp, retrieveRequestPart.lastDownloadedTimestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, state, lastDownloadedTimestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RetrieveRequestPart {\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    lastDownloadedTimestamp: ").append(toIndentedString(lastDownloadedTimestamp)).append("\n");
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
