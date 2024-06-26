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
 * RegisterIngestObjectResponse
 */
@JsonPropertyOrder({
  RegisterIngestObjectResponse.JSON_PROPERTY_INGEST_OBJECT_ID
})
@JsonTypeName("RegisterIngestObjectResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class RegisterIngestObjectResponse {
  public static final String JSON_PROPERTY_INGEST_OBJECT_ID = "ingestObjectId";
  private Long ingestObjectId;

  public RegisterIngestObjectResponse() { 
  }

  public RegisterIngestObjectResponse ingestObjectId(Long ingestObjectId) {
    
    this.ingestObjectId = ingestObjectId;
    return this;
  }

   /**
   * The ID of the object within the batch
   * @return ingestObjectId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10293", value = "The ID of the object within the batch")
  @JsonProperty(JSON_PROPERTY_INGEST_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getIngestObjectId() {
    return ingestObjectId;
  }


  @JsonProperty(JSON_PROPERTY_INGEST_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
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
    RegisterIngestObjectResponse registerIngestObjectResponse = (RegisterIngestObjectResponse) o;
    return Objects.equals(this.ingestObjectId, registerIngestObjectResponse.ingestObjectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ingestObjectId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterIngestObjectResponse {\n");
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

