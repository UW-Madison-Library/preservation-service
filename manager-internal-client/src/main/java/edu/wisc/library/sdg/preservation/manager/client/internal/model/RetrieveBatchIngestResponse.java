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
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchObjectWithFiles;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * RetrieveBatchIngestResponse
 */
@JsonPropertyOrder({
  RetrieveBatchIngestResponse.JSON_PROPERTY_APPROVED_OBJECTS
})
@JsonTypeName("RetrieveBatchIngestResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class RetrieveBatchIngestResponse {
  public static final String JSON_PROPERTY_APPROVED_OBJECTS = "approvedObjects";
  private List<BatchObjectWithFiles> approvedObjects = new ArrayList<>();

  public RetrieveBatchIngestResponse() { 
  }

  public RetrieveBatchIngestResponse approvedObjects(List<BatchObjectWithFiles> approvedObjects) {
    
    this.approvedObjects = approvedObjects;
    return this;
  }

  public RetrieveBatchIngestResponse addApprovedObjectsItem(BatchObjectWithFiles approvedObjectsItem) {
    this.approvedObjects.add(approvedObjectsItem);
    return this;
  }

   /**
   * Objects that need to be ingested
   * @return approvedObjects
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "Objects that need to be ingested")
  @JsonProperty(JSON_PROPERTY_APPROVED_OBJECTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<BatchObjectWithFiles> getApprovedObjects() {
    return approvedObjects;
  }


  @JsonProperty(JSON_PROPERTY_APPROVED_OBJECTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setApprovedObjects(List<BatchObjectWithFiles> approvedObjects) {
    this.approvedObjects = approvedObjects;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RetrieveBatchIngestResponse retrieveBatchIngestResponse = (RetrieveBatchIngestResponse) o;
    return Objects.equals(this.approvedObjects, retrieveBatchIngestResponse.approvedObjects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approvedObjects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RetrieveBatchIngestResponse {\n");
    sb.append("    approvedObjects: ").append(toIndentedString(approvedObjects)).append("\n");
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

