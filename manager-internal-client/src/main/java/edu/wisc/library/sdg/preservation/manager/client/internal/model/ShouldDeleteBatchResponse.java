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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * ShouldDeleteBatchResponse
 */
@JsonPropertyOrder({
  ShouldDeleteBatchResponse.JSON_PROPERTY_INGEST_ID,
  ShouldDeleteBatchResponse.JSON_PROPERTY_VERDICT
})
@JsonTypeName("ShouldDeleteBatchResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class ShouldDeleteBatchResponse {
  public static final String JSON_PROPERTY_INGEST_ID = "ingestId";
  private Long ingestId;

  /**
   * Indicates if the batch should be kept or deleted
   */
  public enum VerdictEnum {
    KEEP("KEEP"),
    
    DELETE("DELETE"),
    
    NOT_FOUND("NOT_FOUND");

    private String value;

    VerdictEnum(String value) {
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
    public static VerdictEnum fromValue(String value) {
      for (VerdictEnum b : VerdictEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_VERDICT = "verdict";
  private VerdictEnum verdict;

  public ShouldDeleteBatchResponse() { 
  }

  public ShouldDeleteBatchResponse ingestId(Long ingestId) {
    
    this.ingestId = ingestId;
    return this;
  }

   /**
   * The ID for the Ingest Batch
   * @return ingestId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10293", value = "The ID for the Ingest Batch")
  @JsonProperty(JSON_PROPERTY_INGEST_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getIngestId() {
    return ingestId;
  }


  @JsonProperty(JSON_PROPERTY_INGEST_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setIngestId(Long ingestId) {
    this.ingestId = ingestId;
  }


  public ShouldDeleteBatchResponse verdict(VerdictEnum verdict) {
    
    this.verdict = verdict;
    return this;
  }

   /**
   * Indicates if the batch should be kept or deleted
   * @return verdict
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "KEEP", value = "Indicates if the batch should be kept or deleted")
  @JsonProperty(JSON_PROPERTY_VERDICT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public VerdictEnum getVerdict() {
    return verdict;
  }


  @JsonProperty(JSON_PROPERTY_VERDICT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setVerdict(VerdictEnum verdict) {
    this.verdict = verdict;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShouldDeleteBatchResponse shouldDeleteBatchResponse = (ShouldDeleteBatchResponse) o;
    return Objects.equals(this.ingestId, shouldDeleteBatchResponse.ingestId) &&
        Objects.equals(this.verdict, shouldDeleteBatchResponse.verdict);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ingestId, verdict);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShouldDeleteBatchResponse {\n");
    sb.append("    ingestId: ").append(toIndentedString(ingestId)).append("\n");
    sb.append("    verdict: ").append(toIndentedString(verdict)).append("\n");
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

