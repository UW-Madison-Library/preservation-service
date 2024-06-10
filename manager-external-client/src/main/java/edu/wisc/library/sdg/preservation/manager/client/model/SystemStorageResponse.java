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
import edu.wisc.library.sdg.preservation.manager.client.model.SystemStorageOrgDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * SystemStorageResponse
 */
@JsonPropertyOrder({
  SystemStorageResponse.JSON_PROPERTY_TOTAL_OBJECT_COUNT,
  SystemStorageResponse.JSON_PROPERTY_TOTAL_STORAGE_MEGABYTES,
  SystemStorageResponse.JSON_PROPERTY_ORGANIZATIONS
})
@JsonTypeName("SystemStorageResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class SystemStorageResponse {
  public static final String JSON_PROPERTY_TOTAL_OBJECT_COUNT = "totalObjectCount";
  private Long totalObjectCount;

  public static final String JSON_PROPERTY_TOTAL_STORAGE_MEGABYTES = "totalStorageMegabytes";
  private Long totalStorageMegabytes;

  public static final String JSON_PROPERTY_ORGANIZATIONS = "organizations";
  private List<SystemStorageOrgDetails> organizations = null;

  public SystemStorageResponse() { 
  }

  public SystemStorageResponse totalObjectCount(Long totalObjectCount) {
    
    this.totalObjectCount = totalObjectCount;
    return this;
  }

   /**
   * The total number of objects in the system
   * @return totalObjectCount
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10293", value = "The total number of objects in the system")
  @JsonProperty(JSON_PROPERTY_TOTAL_OBJECT_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getTotalObjectCount() {
    return totalObjectCount;
  }


  @JsonProperty(JSON_PROPERTY_TOTAL_OBJECT_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTotalObjectCount(Long totalObjectCount) {
    this.totalObjectCount = totalObjectCount;
  }


  public SystemStorageResponse totalStorageMegabytes(Long totalStorageMegabytes) {
    
    this.totalStorageMegabytes = totalStorageMegabytes;
    return this;
  }

   /**
   * The approximate amount of space occupied by all object content files in the system in megabytes
   * @return totalStorageMegabytes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10293", value = "The approximate amount of space occupied by all object content files in the system in megabytes")
  @JsonProperty(JSON_PROPERTY_TOTAL_STORAGE_MEGABYTES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getTotalStorageMegabytes() {
    return totalStorageMegabytes;
  }


  @JsonProperty(JSON_PROPERTY_TOTAL_STORAGE_MEGABYTES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTotalStorageMegabytes(Long totalStorageMegabytes) {
    this.totalStorageMegabytes = totalStorageMegabytes;
  }


  public SystemStorageResponse organizations(List<SystemStorageOrgDetails> organizations) {
    
    this.organizations = organizations;
    return this;
  }

  public SystemStorageResponse addOrganizationsItem(SystemStorageOrgDetails organizationsItem) {
    if (this.organizations == null) {
      this.organizations = new ArrayList<>();
    }
    this.organizations.add(organizationsItem);
    return this;
  }

   /**
   * Organization level storage details
   * @return organizations
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Organization level storage details")
  @JsonProperty(JSON_PROPERTY_ORGANIZATIONS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<SystemStorageOrgDetails> getOrganizations() {
    return organizations;
  }


  @JsonProperty(JSON_PROPERTY_ORGANIZATIONS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOrganizations(List<SystemStorageOrgDetails> organizations) {
    this.organizations = organizations;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemStorageResponse systemStorageResponse = (SystemStorageResponse) o;
    return Objects.equals(this.totalObjectCount, systemStorageResponse.totalObjectCount) &&
        Objects.equals(this.totalStorageMegabytes, systemStorageResponse.totalStorageMegabytes) &&
        Objects.equals(this.organizations, systemStorageResponse.organizations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalObjectCount, totalStorageMegabytes, organizations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemStorageResponse {\n");
    sb.append("    totalObjectCount: ").append(toIndentedString(totalObjectCount)).append("\n");
    sb.append("    totalStorageMegabytes: ").append(toIndentedString(totalStorageMegabytes)).append("\n");
    sb.append("    organizations: ").append(toIndentedString(organizations)).append("\n");
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
