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
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchSummary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * RetrieveBatchResponse
 */
@JsonPropertyOrder({
  RetrieveBatchResponse.JSON_PROPERTY_INGEST_BATCH
})
@JsonTypeName("RetrieveBatchResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class RetrieveBatchResponse {
  public static final String JSON_PROPERTY_INGEST_BATCH = "ingestBatch";
  private IngestBatchSummary ingestBatch;

  public RetrieveBatchResponse() { 
  }

  public RetrieveBatchResponse ingestBatch(IngestBatchSummary ingestBatch) {
    
    this.ingestBatch = ingestBatch;
    return this;
  }

   /**
   * Get ingestBatch
   * @return ingestBatch
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_INGEST_BATCH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public IngestBatchSummary getIngestBatch() {
    return ingestBatch;
  }


  @JsonProperty(JSON_PROPERTY_INGEST_BATCH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setIngestBatch(IngestBatchSummary ingestBatch) {
    this.ingestBatch = ingestBatch;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RetrieveBatchResponse retrieveBatchResponse = (RetrieveBatchResponse) o;
    return Objects.equals(this.ingestBatch, retrieveBatchResponse.ingestBatch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ingestBatch);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RetrieveBatchResponse {\n");
    sb.append("    ingestBatch: ").append(toIndentedString(ingestBatch)).append("\n");
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

