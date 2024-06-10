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
import edu.wisc.library.sdg.preservation.manager.client.model.UserType;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Details about a user in a vault
 */
@ApiModel(description = "Details about a user in a vault")
@JsonPropertyOrder({
  VaultUser.JSON_PROPERTY_USERNAME,
  VaultUser.JSON_PROPERTY_DISPLAY_NAME,
  VaultUser.JSON_PROPERTY_ENABLED_IN_ORG,
  VaultUser.JSON_PROPERTY_USER_TYPE,
  VaultUser.JSON_PROPERTY_PERMISSIONS
})
@JsonTypeName("VaultUser")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class VaultUser {
  public static final String JSON_PROPERTY_USERNAME = "username";
  private String username;

  public static final String JSON_PROPERTY_DISPLAY_NAME = "displayName";
  private String displayName;

  public static final String JSON_PROPERTY_ENABLED_IN_ORG = "enabledInOrg";
  private Boolean enabledInOrg;

  public static final String JSON_PROPERTY_USER_TYPE = "userType";
  private UserType userType;

  public static final String JSON_PROPERTY_PERMISSIONS = "permissions";
  private List<VaultPermission> permissions = null;

  public VaultUser() { 
  }

  public VaultUser username(String username) {
    
    this.username = username;
    return this;
  }

   /**
   * A user&#39;s username in the preservation system
   * @return username
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "A user's username in the preservation system")
  @JsonProperty(JSON_PROPERTY_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getUsername() {
    return username;
  }


  @JsonProperty(JSON_PROPERTY_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUsername(String username) {
    this.username = username;
  }


  public VaultUser displayName(String displayName) {
    
    this.displayName = displayName;
    return this;
  }

   /**
   * The user&#39;s display name
   * @return displayName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The user's display name")
  @JsonProperty(JSON_PROPERTY_DISPLAY_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getDisplayName() {
    return displayName;
  }


  @JsonProperty(JSON_PROPERTY_DISPLAY_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }


  public VaultUser enabledInOrg(Boolean enabledInOrg) {
    
    this.enabledInOrg = enabledInOrg;
    return this;
  }

   /**
   * Indicates if the user is enabled
   * @return enabledInOrg
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "true", value = "Indicates if the user is enabled")
  @JsonProperty(JSON_PROPERTY_ENABLED_IN_ORG)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getEnabledInOrg() {
    return enabledInOrg;
  }


  @JsonProperty(JSON_PROPERTY_ENABLED_IN_ORG)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEnabledInOrg(Boolean enabledInOrg) {
    this.enabledInOrg = enabledInOrg;
  }


  public VaultUser userType(UserType userType) {
    
    this.userType = userType;
    return this;
  }

   /**
   * Get userType
   * @return userType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_USER_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public UserType getUserType() {
    return userType;
  }


  @JsonProperty(JSON_PROPERTY_USER_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUserType(UserType userType) {
    this.userType = userType;
  }


  public VaultUser permissions(List<VaultPermission> permissions) {
    
    this.permissions = permissions;
    return this;
  }

  public VaultUser addPermissionsItem(VaultPermission permissionsItem) {
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
    VaultUser vaultUser = (VaultUser) o;
    return Objects.equals(this.username, vaultUser.username) &&
        Objects.equals(this.displayName, vaultUser.displayName) &&
        Objects.equals(this.enabledInOrg, vaultUser.enabledInOrg) &&
        Objects.equals(this.userType, vaultUser.userType) &&
        Objects.equals(this.permissions, vaultUser.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, displayName, enabledInOrg, userType, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VaultUser {\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    enabledInOrg: ").append(toIndentedString(enabledInOrg)).append("\n");
    sb.append("    userType: ").append(toIndentedString(userType)).append("\n");
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

