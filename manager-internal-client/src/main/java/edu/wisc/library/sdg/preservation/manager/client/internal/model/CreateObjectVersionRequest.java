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
 * CreateObjectVersionRequest
 */
@JsonPropertyOrder({
  CreateObjectVersionRequest.JSON_PROPERTY_OBJECT_ID,
  CreateObjectVersionRequest.JSON_PROPERTY_INGEST_OBJECT_ID
})
@JsonTypeName("CreateObjectVersionRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class CreateObjectVersionRequest {
  public static final String JSON_PROPERTY_OBJECT_ID = "objectId";
  private String objectId;

  public static final String JSON_PROPERTY_INGEST_OBJECT_ID = "ingestObjectId";
  private Long ingestObjectId;

  public CreateObjectVersionRequest() { 
  }

  public CreateObjectVersionRequest objectId(String objectId) {
    
    this.objectId = objectId;
    return this;
  }

   /**
   * The internal ID of the object
   * @return objectId
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "urn:uuid:3c658673-38aa-4425-b931-2971c45256d3", required = true, value = "The internal ID of the object")
  @JsonProperty(JSON_PROPERTY_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getObjectId() {
    return objectId;
  }


  @JsonProperty(JSON_PROPERTY_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }


  public CreateObjectVersionRequest ingestObjectId(Long ingestObjectId) {
    
    this.ingestObjectId = ingestObjectId;
    return this;
  }

   /**
   * The ID of the object within the batch
   * @return ingestObjectId
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "10293", required = true, value = "The ID of the object within the batch")
  @JsonProperty(JSON_PROPERTY_INGEST_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getIngestObjectId() {
    return ingestObjectId;
  }


  @JsonProperty(JSON_PROPERTY_INGEST_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setIngestObjectId(Long ingestObjectId) {
    this.ingestObjectId = ingestObjectId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateObjectVersionRequest createObjectVersionRequest = (CreateObjectVersionRequest) o;
    return Objects.equals(this.objectId, createObjectVersionRequest.objectId) &&
        Objects.equals(this.ingestObjectId, createObjectVersionRequest.ingestObjectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectId, ingestObjectId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateObjectVersionRequest {\n");
    sb.append("    objectId: ").append(toIndentedString(objectId)).append("\n");
    sb.append("    ingestObjectId: ").append(toIndentedString(ingestObjectId)).append("\n");
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

