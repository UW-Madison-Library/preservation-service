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
import edu.wisc.library.sdg.preservation.manager.client.model.OrgAuthority;
import edu.wisc.library.sdg.preservation.manager.client.model.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * DescribeOrgResponse
 */
@JsonPropertyOrder({
  DescribeOrgResponse.JSON_PROPERTY_ORG_NAME,
  DescribeOrgResponse.JSON_PROPERTY_DISPLAY_NAME,
  DescribeOrgResponse.JSON_PROPERTY_ROLE,
  DescribeOrgResponse.JSON_PROPERTY_AUTHORITIES
})
@JsonTypeName("DescribeOrgResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class DescribeOrgResponse {
  public static final String JSON_PROPERTY_ORG_NAME = "orgName";
  private String orgName;

  public static final String JSON_PROPERTY_DISPLAY_NAME = "displayName";
  private String displayName;

  public static final String JSON_PROPERTY_ROLE = "role";
  private Role role;

  public static final String JSON_PROPERTY_AUTHORITIES = "authorities";
  private List<OrgAuthority> authorities = null;

  public DescribeOrgResponse() { 
  }

  public DescribeOrgResponse orgName(String orgName) {
    
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


  public DescribeOrgResponse displayName(String displayName) {
    
    this.displayName = displayName;
    return this;
  }

   /**
   * The display name of the organization
   * @return displayName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "Organization Name", value = "The display name of the organization")
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


  public DescribeOrgResponse role(Role role) {
    
    this.role = role;
    return this;
  }

   /**
   * Get role
   * @return role
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_ROLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Role getRole() {
    return role;
  }


  @JsonProperty(JSON_PROPERTY_ROLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRole(Role role) {
    this.role = role;
  }


  public DescribeOrgResponse authorities(List<OrgAuthority> authorities) {
    
    this.authorities = authorities;
    return this;
  }

  public DescribeOrgResponse addAuthoritiesItem(OrgAuthority authoritiesItem) {
    if (this.authorities == null) {
      this.authorities = new ArrayList<>();
    }
    this.authorities.add(authoritiesItem);
    return this;
  }

   /**
   * List of the user&#39;s authorities within the org
   * @return authorities
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "List of the user's authorities within the org")
  @JsonProperty(JSON_PROPERTY_AUTHORITIES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<OrgAuthority> getAuthorities() {
    return authorities;
  }


  @JsonProperty(JSON_PROPERTY_AUTHORITIES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthorities(List<OrgAuthority> authorities) {
    this.authorities = authorities;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DescribeOrgResponse describeOrgResponse = (DescribeOrgResponse) o;
    return Objects.equals(this.orgName, describeOrgResponse.orgName) &&
        Objects.equals(this.displayName, describeOrgResponse.displayName) &&
        Objects.equals(this.role, describeOrgResponse.role) &&
        Objects.equals(this.authorities, describeOrgResponse.authorities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orgName, displayName, role, authorities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DescribeOrgResponse {\n");
    sb.append("    orgName: ").append(toIndentedString(orgName)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    authorities: ").append(toIndentedString(authorities)).append("\n");
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

