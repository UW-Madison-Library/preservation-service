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
import edu.wisc.library.sdg.preservation.manager.client.model.EncodingDiff;
import edu.wisc.library.sdg.preservation.manager.client.model.FormatDiff;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidityDiff;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * FileDiff
 */
@JsonPropertyOrder({
  FileDiff.JSON_PROPERTY_PATH,
  FileDiff.JSON_PROPERTY_NEW_SHA256_DIGEST,
  FileDiff.JSON_PROPERTY_OLD_SHA256_DIGEST,
  FileDiff.JSON_PROPERTY_DIFF,
  FileDiff.JSON_PROPERTY_FORMAT_DIFF,
  FileDiff.JSON_PROPERTY_ENCODING_DIFF,
  FileDiff.JSON_PROPERTY_VALIDITY_DIFF
})
@JsonTypeName("FileDiff")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class FileDiff {
  public static final String JSON_PROPERTY_PATH = "path";
  private String path;

  public static final String JSON_PROPERTY_NEW_SHA256_DIGEST = "newSha256Digest";
  private String newSha256Digest;

  public static final String JSON_PROPERTY_OLD_SHA256_DIGEST = "oldSha256Digest";
  private String oldSha256Digest;

  /**
   * The relationship between the batch object file and the preserved object file
   */
  public enum DiffEnum {
    ADDED("ADDED"),
    
    REMOVED("REMOVED"),
    
    MODIFIED("MODIFIED"),
    
    UNCHANGED("UNCHANGED");

    private String value;

    DiffEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DiffEnum fromValue(String value) {
      for (DiffEnum b : DiffEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_DIFF = "diff";
  private DiffEnum diff;

  public static final String JSON_PROPERTY_FORMAT_DIFF = "formatDiff";
  private FormatDiff formatDiff;

  public static final String JSON_PROPERTY_ENCODING_DIFF = "encodingDiff";
  private EncodingDiff encodingDiff;

  public static final String JSON_PROPERTY_VALIDITY_DIFF = "validityDiff";
  private ValidityDiff validityDiff;

  public FileDiff() { 
  }

  public FileDiff path(String path) {
    
    this.path = path;
    return this;
  }

   /**
   * The object relative path of the file
   * @return path
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "path/to/file.txt", value = "The object relative path of the file")
  @JsonProperty(JSON_PROPERTY_PATH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPath() {
    return path;
  }


  @JsonProperty(JSON_PROPERTY_PATH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPath(String path) {
    this.path = path;
  }


  public FileDiff newSha256Digest(String newSha256Digest) {
    
    this.newSha256Digest = newSha256Digest;
    return this;
  }

   /**
   * The sha256 digest of the file in the batch
   * @return newSha256Digest
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1ff0a30eedd7819f4552179c94ea89ed03f5d1a4265fcd0f971ebe7bc17317cf", value = "The sha256 digest of the file in the batch")
  @JsonProperty(JSON_PROPERTY_NEW_SHA256_DIGEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getNewSha256Digest() {
    return newSha256Digest;
  }


  @JsonProperty(JSON_PROPERTY_NEW_SHA256_DIGEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setNewSha256Digest(String newSha256Digest) {
    this.newSha256Digest = newSha256Digest;
  }


  public FileDiff oldSha256Digest(String oldSha256Digest) {
    
    this.oldSha256Digest = oldSha256Digest;
    return this;
  }

   /**
   * The sha256 digest of the file in the preserved object
   * @return oldSha256Digest
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1ff0a30eedd7819f4552179c94ea89ed03f5d1a4265fcd0f971ebe7bc17317cf", value = "The sha256 digest of the file in the preserved object")
  @JsonProperty(JSON_PROPERTY_OLD_SHA256_DIGEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOldSha256Digest() {
    return oldSha256Digest;
  }


  @JsonProperty(JSON_PROPERTY_OLD_SHA256_DIGEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOldSha256Digest(String oldSha256Digest) {
    this.oldSha256Digest = oldSha256Digest;
  }


  public FileDiff diff(DiffEnum diff) {
    
    this.diff = diff;
    return this;
  }

   /**
   * The relationship between the batch object file and the preserved object file
   * @return diff
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The relationship between the batch object file and the preserved object file")
  @JsonProperty(JSON_PROPERTY_DIFF)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public DiffEnum getDiff() {
    return diff;
  }


  @JsonProperty(JSON_PROPERTY_DIFF)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDiff(DiffEnum diff) {
    this.diff = diff;
  }


  public FileDiff formatDiff(FormatDiff formatDiff) {
    
    this.formatDiff = formatDiff;
    return this;
  }

   /**
   * Get formatDiff
   * @return formatDiff
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_FORMAT_DIFF)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public FormatDiff getFormatDiff() {
    return formatDiff;
  }


  @JsonProperty(JSON_PROPERTY_FORMAT_DIFF)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFormatDiff(FormatDiff formatDiff) {
    this.formatDiff = formatDiff;
  }


  public FileDiff encodingDiff(EncodingDiff encodingDiff) {
    
    this.encodingDiff = encodingDiff;
    return this;
  }

   /**
   * Get encodingDiff
   * @return encodingDiff
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_ENCODING_DIFF)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EncodingDiff getEncodingDiff() {
    return encodingDiff;
  }


  @JsonProperty(JSON_PROPERTY_ENCODING_DIFF)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEncodingDiff(EncodingDiff encodingDiff) {
    this.encodingDiff = encodingDiff;
  }


  public FileDiff validityDiff(ValidityDiff validityDiff) {
    
    this.validityDiff = validityDiff;
    return this;
  }

   /**
   * Get validityDiff
   * @return validityDiff
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_VALIDITY_DIFF)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ValidityDiff getValidityDiff() {
    return validityDiff;
  }


  @JsonProperty(JSON_PROPERTY_VALIDITY_DIFF)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setValidityDiff(ValidityDiff validityDiff) {
    this.validityDiff = validityDiff;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FileDiff fileDiff = (FileDiff) o;
    return Objects.equals(this.path, fileDiff.path) &&
        Objects.equals(this.newSha256Digest, fileDiff.newSha256Digest) &&
        Objects.equals(this.oldSha256Digest, fileDiff.oldSha256Digest) &&
        Objects.equals(this.diff, fileDiff.diff) &&
        Objects.equals(this.formatDiff, fileDiff.formatDiff) &&
        Objects.equals(this.encodingDiff, fileDiff.encodingDiff) &&
        Objects.equals(this.validityDiff, fileDiff.validityDiff);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, newSha256Digest, oldSha256Digest, diff, formatDiff, encodingDiff, validityDiff);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FileDiff {\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    newSha256Digest: ").append(toIndentedString(newSha256Digest)).append("\n");
    sb.append("    oldSha256Digest: ").append(toIndentedString(oldSha256Digest)).append("\n");
    sb.append("    diff: ").append(toIndentedString(diff)).append("\n");
    sb.append("    formatDiff: ").append(toIndentedString(formatDiff)).append("\n");
    sb.append("    encodingDiff: ").append(toIndentedString(encodingDiff)).append("\n");
    sb.append("    validityDiff: ").append(toIndentedString(validityDiff)).append("\n");
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

