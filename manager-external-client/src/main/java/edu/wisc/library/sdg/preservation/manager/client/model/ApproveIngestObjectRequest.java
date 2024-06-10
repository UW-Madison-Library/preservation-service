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
 * ApproveIngestObjectRequest
 */
@JsonPropertyOrder({
  ApproveIngestObjectRequest.JSON_PROPERTY_INGEST_ID,
  ApproveIngestObjectRequest.JSON_PROPERTY_EXTERNAL_OBJECT_ID,
  ApproveIngestObjectRequest.JSON_PROPERTY_REVIEWED_TIMESTAMP
})
@JsonTypeName("ApproveIngestObjectRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class ApproveIngestObjectRequest {
  public static final String JSON_PROPERTY_INGEST_ID = "ingestId";
  private Long ingestId;

  public static final String JSON_PROPERTY_EXTERNAL_OBJECT_ID = "externalObjectId";
  private String externalObjectId;

  public static final String JSON_PROPERTY_REVIEWED_TIMESTAMP = "reviewedTimestamp";
  private OffsetDateTime reviewedTimestamp;

  public ApproveIngestObjectRequest() { 
  }

  public ApproveIngestObjectRequest ingestId(Long ingestId) {
    
    this.ingestId = ingestId;
    return this;
  }

   /**
   * The ID for the Ingest Batch
   * @return ingestId
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "10293", required = true, value = "The ID for the Ingest Batch")
  @JsonProperty(JSON_PROPERTY_INGEST_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getIngestId() {
    return ingestId;
  }


  @JsonProperty(JSON_PROPERTY_INGEST_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setIngestId(Long ingestId) {
    this.ingestId = ingestId;
  }


  public ApproveIngestObjectRequest externalObjectId(String externalObjectId) {
    
    this.externalObjectId = externalObjectId;
    return this;
  }

   /**
   * The external ID of the object
   * @return externalObjectId
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "1711.dl/ZQ5WPGU2GKBFT8Q", required = true, value = "The external ID of the object")
  @JsonProperty(JSON_PROPERTY_EXTERNAL_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getExternalObjectId() {
    return externalObjectId;
  }


  @JsonProperty(JSON_PROPERTY_EXTERNAL_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setExternalObjectId(String externalObjectId) {
    this.externalObjectId = externalObjectId;
  }


  public ApproveIngestObjectRequest reviewedTimestamp(OffsetDateTime reviewedTimestamp) {
    
    this.reviewedTimestamp = reviewedTimestamp;
    return this;
  }

   /**
   * Timestamp the object changes were reviewed. Used to ensure that the object was not changed since then. Can be null to use the current time.
   * @return reviewedTimestamp
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Timestamp the object changes were reviewed. Used to ensure that the object was not changed since then. Can be null to use the current time.")
  @JsonProperty(JSON_PROPERTY_REVIEWED_TIMESTAMP)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public OffsetDateTime getReviewedTimestamp() {
    return reviewedTimestamp;
  }


  @JsonProperty(JSON_PROPERTY_REVIEWED_TIMESTAMP)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReviewedTimestamp(OffsetDateTime reviewedTimestamp) {
    this.reviewedTimestamp = reviewedTimestamp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApproveIngestObjectRequest approveIngestObjectRequest = (ApproveIngestObjectRequest) o;
    return Objects.equals(this.ingestId, approveIngestObjectRequest.ingestId) &&
        Objects.equals(this.externalObjectId, approveIngestObjectRequest.externalObjectId) &&
        Objects.equals(this.reviewedTimestamp, approveIngestObjectRequest.reviewedTimestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ingestId, externalObjectId, reviewedTimestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApproveIngestObjectRequest {\n");
    sb.append("    ingestId: ").append(toIndentedString(ingestId)).append("\n");
    sb.append("    externalObjectId: ").append(toIndentedString(externalObjectId)).append("\n");
    sb.append("    reviewedTimestamp: ").append(toIndentedString(reviewedTimestamp)).append("\n");
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
