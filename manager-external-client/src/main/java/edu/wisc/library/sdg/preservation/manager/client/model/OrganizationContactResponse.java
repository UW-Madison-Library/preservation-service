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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * OrganizationContactResponse
 */
@JsonPropertyOrder({
  OrganizationContactResponse.JSON_PROPERTY_CONTACT_NAME,
  OrganizationContactResponse.JSON_PROPERTY_CONTACT_EMAIL,
  OrganizationContactResponse.JSON_PROPERTY_CONTACT_PHONE
})
@JsonTypeName("OrganizationContactResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class OrganizationContactResponse {
  public static final String JSON_PROPERTY_CONTACT_NAME = "contactName";
  private String contactName;

  public static final String JSON_PROPERTY_CONTACT_EMAIL = "contactEmail";
  private String contactEmail;

  public static final String JSON_PROPERTY_CONTACT_PHONE = "contactPhone";
  private String contactPhone;

  public OrganizationContactResponse() { 
  }

  public OrganizationContactResponse contactName(String contactName) {
    
    this.contactName = contactName;
    return this;
  }

   /**
   * The primary contact for an organization
   * @return contactName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The primary contact for an organization")
  @JsonProperty(JSON_PROPERTY_CONTACT_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getContactName() {
    return contactName;
  }


  @JsonProperty(JSON_PROPERTY_CONTACT_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setContactName(String contactName) {
    this.contactName = contactName;
  }


  public OrganizationContactResponse contactEmail(String contactEmail) {
    
    this.contactEmail = contactEmail;
    return this;
  }

   /**
   * The email of the primary contact for an organization
   * @return contactEmail
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "test@example.com", value = "The email of the primary contact for an organization")
  @JsonProperty(JSON_PROPERTY_CONTACT_EMAIL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getContactEmail() {
    return contactEmail;
  }


  @JsonProperty(JSON_PROPERTY_CONTACT_EMAIL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }


  public OrganizationContactResponse contactPhone(String contactPhone) {
    
    this.contactPhone = contactPhone;
    return this;
  }

   /**
   * The phone number for the primary contact for an organization
   * @return contactPhone
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "555-555-5555", value = "The phone number for the primary contact for an organization")
  @JsonProperty(JSON_PROPERTY_CONTACT_PHONE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getContactPhone() {
    return contactPhone;
  }


  @JsonProperty(JSON_PROPERTY_CONTACT_PHONE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationContactResponse organizationContactResponse = (OrganizationContactResponse) o;
    return Objects.equals(this.contactName, organizationContactResponse.contactName) &&
        Objects.equals(this.contactEmail, organizationContactResponse.contactEmail) &&
        Objects.equals(this.contactPhone, organizationContactResponse.contactPhone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contactName, contactEmail, contactPhone);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationContactResponse {\n");
    sb.append("    contactName: ").append(toIndentedString(contactName)).append("\n");
    sb.append("    contactEmail: ").append(toIndentedString(contactEmail)).append("\n");
    sb.append("    contactPhone: ").append(toIndentedString(contactPhone)).append("\n");
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

