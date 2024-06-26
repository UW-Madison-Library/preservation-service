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
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * RetrieveBatchObjectsResponse
 */
@JsonPropertyOrder({
  RetrieveBatchObjectsResponse.JSON_PROPERTY_PAGE,
  RetrieveBatchObjectsResponse.JSON_PROPERTY_TOTAL_PAGES,
  RetrieveBatchObjectsResponse.JSON_PROPERTY_TOTAL_RESULTS,
  RetrieveBatchObjectsResponse.JSON_PROPERTY_BATCH_OBJECTS
})
@JsonTypeName("RetrieveBatchObjectsResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class RetrieveBatchObjectsResponse {
  public static final String JSON_PROPERTY_PAGE = "page";
  private Integer page;

  public static final String JSON_PROPERTY_TOTAL_PAGES = "totalPages";
  private Integer totalPages;

  public static final String JSON_PROPERTY_TOTAL_RESULTS = "totalResults";
  private Integer totalResults;

  public static final String JSON_PROPERTY_BATCH_OBJECTS = "batchObjects";
  private List<IngestBatchObject> batchObjects = new ArrayList<>();

  public RetrieveBatchObjectsResponse() { 
  }

  public RetrieveBatchObjectsResponse page(Integer page) {
    
    this.page = page;
    return this;
  }

   /**
   * The page number of this page, zero indexed
   * @return page
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "0", required = true, value = "The page number of this page, zero indexed")
  @JsonProperty(JSON_PROPERTY_PAGE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getPage() {
    return page;
  }


  @JsonProperty(JSON_PROPERTY_PAGE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPage(Integer page) {
    this.page = page;
  }


  public RetrieveBatchObjectsResponse totalPages(Integer totalPages) {
    
    this.totalPages = totalPages;
    return this;
  }

   /**
   * The total number of pages
   * @return totalPages
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "10", required = true, value = "The total number of pages")
  @JsonProperty(JSON_PROPERTY_TOTAL_PAGES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getTotalPages() {
    return totalPages;
  }


  @JsonProperty(JSON_PROPERTY_TOTAL_PAGES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }


  public RetrieveBatchObjectsResponse totalResults(Integer totalResults) {
    
    this.totalResults = totalResults;
    return this;
  }

   /**
   * The total number of results found
   * @return totalResults
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "100", required = true, value = "The total number of results found")
  @JsonProperty(JSON_PROPERTY_TOTAL_RESULTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getTotalResults() {
    return totalResults;
  }


  @JsonProperty(JSON_PROPERTY_TOTAL_RESULTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setTotalResults(Integer totalResults) {
    this.totalResults = totalResults;
  }


  public RetrieveBatchObjectsResponse batchObjects(List<IngestBatchObject> batchObjects) {
    
    this.batchObjects = batchObjects;
    return this;
  }

  public RetrieveBatchObjectsResponse addBatchObjectsItem(IngestBatchObject batchObjectsItem) {
    this.batchObjects.add(batchObjectsItem);
    return this;
  }

   /**
   * The objects associated to the batch
   * @return batchObjects
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "The objects associated to the batch")
  @JsonProperty(JSON_PROPERTY_BATCH_OBJECTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<IngestBatchObject> getBatchObjects() {
    return batchObjects;
  }


  @JsonProperty(JSON_PROPERTY_BATCH_OBJECTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBatchObjects(List<IngestBatchObject> batchObjects) {
    this.batchObjects = batchObjects;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RetrieveBatchObjectsResponse retrieveBatchObjectsResponse = (RetrieveBatchObjectsResponse) o;
    return Objects.equals(this.page, retrieveBatchObjectsResponse.page) &&
        Objects.equals(this.totalPages, retrieveBatchObjectsResponse.totalPages) &&
        Objects.equals(this.totalResults, retrieveBatchObjectsResponse.totalResults) &&
        Objects.equals(this.batchObjects, retrieveBatchObjectsResponse.batchObjects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, totalPages, totalResults, batchObjects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RetrieveBatchObjectsResponse {\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
    sb.append("    batchObjects: ").append(toIndentedString(batchObjects)).append("\n");
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

