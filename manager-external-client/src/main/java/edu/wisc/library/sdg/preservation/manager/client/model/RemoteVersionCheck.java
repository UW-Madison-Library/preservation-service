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
import edu.wisc.library.sdg.preservation.manager.client.model.DataStore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * RemoteVersionCheck
 */
@JsonPropertyOrder({
  RemoteVersionCheck.JSON_PROPERTY_LOCATION,
  RemoteVersionCheck.JSON_PROPERTY_LAST_CHECK
})
@JsonTypeName("RemoteVersionCheck")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class RemoteVersionCheck {
  public static final String JSON_PROPERTY_LOCATION = "location";
  private DataStore location;

  public static final String JSON_PROPERTY_LAST_CHECK = "lastCheck";
  private OffsetDateTime lastCheck;

  public RemoteVersionCheck() { 
  }

  public RemoteVersionCheck location(DataStore location) {
    
    this.location = location;
    return this;
  }

   /**
   * Get location
   * @return location
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_LOCATION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public DataStore getLocation() {
    return location;
  }


  @JsonProperty(JSON_PROPERTY_LOCATION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setLocation(DataStore location) {
    this.location = location;
  }


  public RemoteVersionCheck lastCheck(OffsetDateTime lastCheck) {
    
    this.lastCheck = lastCheck;
    return this;
  }

   /**
   * Timestamp of last validation check
   * @return lastCheck
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Timestamp of last validation check")
  @JsonProperty(JSON_PROPERTY_LAST_CHECK)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public OffsetDateTime getLastCheck() {
    return lastCheck;
  }


  @JsonProperty(JSON_PROPERTY_LAST_CHECK)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setLastCheck(OffsetDateTime lastCheck) {
    this.lastCheck = lastCheck;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemoteVersionCheck remoteVersionCheck = (RemoteVersionCheck) o;
    return Objects.equals(this.location, remoteVersionCheck.location) &&
        Objects.equals(this.lastCheck, remoteVersionCheck.lastCheck);
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, lastCheck);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RemoteVersionCheck {\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    lastCheck: ").append(toIndentedString(lastCheck)).append("\n");
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
