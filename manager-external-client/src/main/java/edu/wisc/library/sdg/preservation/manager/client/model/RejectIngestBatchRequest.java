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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * RejectIngestBatchRequest
 */
@JsonPropertyOrder({
  RejectIngestBatchRequest.JSON_PROPERTY_INGEST_ID
})
@JsonTypeName("RejectIngestBatchRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class RejectIngestBatchRequest {
  public static final String JSON_PROPERTY_INGEST_ID = "ingestId";
  private Long ingestId;

  public RejectIngestBatchRequest() { 
  }

  public RejectIngestBatchRequest ingestId(Long ingestId) {
    
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RejectIngestBatchRequest rejectIngestBatchRequest = (RejectIngestBatchRequest) o;
    return Objects.equals(this.ingestId, rejectIngestBatchRequest.ingestId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ingestId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RejectIngestBatchRequest {\n");
    sb.append("    ingestId: ").append(toIndentedString(ingestId)).append("\n");
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

