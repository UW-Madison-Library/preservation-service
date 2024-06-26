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
import edu.wisc.library.sdg.preservation.manager.client.model.IngestObjectState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * UpdateIngestObjectStateRequest
 */
@JsonPropertyOrder({
  UpdateIngestObjectStateRequest.JSON_PROPERTY_INGEST_ID,
  UpdateIngestObjectStateRequest.JSON_PROPERTY_EXTERNAL_OBJECT_ID,
  UpdateIngestObjectStateRequest.JSON_PROPERTY_STATE
})
@JsonTypeName("UpdateIngestObjectStateRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class UpdateIngestObjectStateRequest {
  public static final String JSON_PROPERTY_INGEST_ID = "ingestId";
  private Long ingestId;

  public static final String JSON_PROPERTY_EXTERNAL_OBJECT_ID = "externalObjectId";
  private String externalObjectId;

  public static final String JSON_PROPERTY_STATE = "state";
  private IngestObjectState state;

  public UpdateIngestObjectStateRequest() { 
  }

  public UpdateIngestObjectStateRequest ingestId(Long ingestId) {
    
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


  public UpdateIngestObjectStateRequest externalObjectId(String externalObjectId) {
    
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


  public UpdateIngestObjectStateRequest state(IngestObjectState state) {
    
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_STATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public IngestObjectState getState() {
    return state;
  }


  @JsonProperty(JSON_PROPERTY_STATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setState(IngestObjectState state) {
    this.state = state;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateIngestObjectStateRequest updateIngestObjectStateRequest = (UpdateIngestObjectStateRequest) o;
    return Objects.equals(this.ingestId, updateIngestObjectStateRequest.ingestId) &&
        Objects.equals(this.externalObjectId, updateIngestObjectStateRequest.externalObjectId) &&
        Objects.equals(this.state, updateIngestObjectStateRequest.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ingestId, externalObjectId, state);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateIngestObjectStateRequest {\n");
    sb.append("    ingestId: ").append(toIndentedString(ingestId)).append("\n");
    sb.append("    externalObjectId: ").append(toIndentedString(externalObjectId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
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

