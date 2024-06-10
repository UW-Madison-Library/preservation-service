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
import edu.wisc.library.sdg.preservation.manager.client.model.VaultObject;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * DescribeVaultResponse
 */
@JsonPropertyOrder({
  DescribeVaultResponse.JSON_PROPERTY_ORG_NAME,
  DescribeVaultResponse.JSON_PROPERTY_VAULT,
  DescribeVaultResponse.JSON_PROPERTY_OBJECT_COUNT,
  DescribeVaultResponse.JSON_PROPERTY_APPROXIMATE_STORAGE_MEGABYTES,
  DescribeVaultResponse.JSON_PROPERTY_PERMISSIONS
})
@JsonTypeName("DescribeVaultResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class DescribeVaultResponse {
  public static final String JSON_PROPERTY_ORG_NAME = "orgName";
  private String orgName;

  public static final String JSON_PROPERTY_VAULT = "vault";
  private VaultObject vault;

  public static final String JSON_PROPERTY_OBJECT_COUNT = "objectCount";
  private Long objectCount;

  public static final String JSON_PROPERTY_APPROXIMATE_STORAGE_MEGABYTES = "approximateStorageMegabytes";
  private Long approximateStorageMegabytes;

  public static final String JSON_PROPERTY_PERMISSIONS = "permissions";
  private List<VaultPermission> permissions = null;

  public DescribeVaultResponse() { 
  }

  public DescribeVaultResponse orgName(String orgName) {
    
    this.orgName = orgName;
    return this;
  }

   /**
   * The ID of the organization
   * @return orgName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "organization-id", value = "The ID of the organization")
  @JsonProperty(JSON_PROPERTY_ORG_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOrgName() {
    return orgName;
  }


  @JsonProperty(JSON_PROPERTY_ORG_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }


  public DescribeVaultResponse vault(VaultObject vault) {
    
    this.vault = vault;
    return this;
  }

   /**
   * Get vault
   * @return vault
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_VAULT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public VaultObject getVault() {
    return vault;
  }


  @JsonProperty(JSON_PROPERTY_VAULT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setVault(VaultObject vault) {
    this.vault = vault;
  }


  public DescribeVaultResponse objectCount(Long objectCount) {
    
    this.objectCount = objectCount;
    return this;
  }

   /**
   * The number of objects in the vault
   * @return objectCount
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10293", value = "The number of objects in the vault")
  @JsonProperty(JSON_PROPERTY_OBJECT_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getObjectCount() {
    return objectCount;
  }


  @JsonProperty(JSON_PROPERTY_OBJECT_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setObjectCount(Long objectCount) {
    this.objectCount = objectCount;
  }


  public DescribeVaultResponse approximateStorageMegabytes(Long approximateStorageMegabytes) {
    
    this.approximateStorageMegabytes = approximateStorageMegabytes;
    return this;
  }

   /**
   * The approximate amount of space occupied by all object content files in the vault in megabytes
   * @return approximateStorageMegabytes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10293", value = "The approximate amount of space occupied by all object content files in the vault in megabytes")
  @JsonProperty(JSON_PROPERTY_APPROXIMATE_STORAGE_MEGABYTES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getApproximateStorageMegabytes() {
    return approximateStorageMegabytes;
  }


  @JsonProperty(JSON_PROPERTY_APPROXIMATE_STORAGE_MEGABYTES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setApproximateStorageMegabytes(Long approximateStorageMegabytes) {
    this.approximateStorageMegabytes = approximateStorageMegabytes;
  }


  public DescribeVaultResponse permissions(List<VaultPermission> permissions) {
    
    this.permissions = permissions;
    return this;
  }

  public DescribeVaultResponse addPermissionsItem(VaultPermission permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

   /**
   * The permissions the user has in the vault
   * @return permissions
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The permissions the user has in the vault")
  @JsonProperty(JSON_PROPERTY_PERMISSIONS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<VaultPermission> getPermissions() {
    return permissions;
  }


  @JsonProperty(JSON_PROPERTY_PERMISSIONS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPermissions(List<VaultPermission> permissions) {
    this.permissions = permissions;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DescribeVaultResponse describeVaultResponse = (DescribeVaultResponse) o;
    return Objects.equals(this.orgName, describeVaultResponse.orgName) &&
        Objects.equals(this.vault, describeVaultResponse.vault) &&
        Objects.equals(this.objectCount, describeVaultResponse.objectCount) &&
        Objects.equals(this.approximateStorageMegabytes, describeVaultResponse.approximateStorageMegabytes) &&
        Objects.equals(this.permissions, describeVaultResponse.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orgName, vault, objectCount, approximateStorageMegabytes, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DescribeVaultResponse {\n");
    sb.append("    orgName: ").append(toIndentedString(orgName)).append("\n");
    sb.append("    vault: ").append(toIndentedString(vault)).append("\n");
    sb.append("    objectCount: ").append(toIndentedString(objectCount)).append("\n");
    sb.append("    approximateStorageMegabytes: ").append(toIndentedString(approximateStorageMegabytes)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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

