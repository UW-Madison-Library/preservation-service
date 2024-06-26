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
 * A file within an object within an Ingest Batch
 */
@ApiModel(description = "A file within an object within an Ingest Batch")
@JsonPropertyOrder({
  ObjectFile.JSON_PROPERTY_FILE_PATH,
  ObjectFile.JSON_PROPERTY_SHA256_DIGEST,
  ObjectFile.JSON_PROPERTY_FILE_SIZE
})
@JsonTypeName("ObjectFile")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class ObjectFile {
  public static final String JSON_PROPERTY_FILE_PATH = "filePath";
  private String filePath;

  public static final String JSON_PROPERTY_SHA256_DIGEST = "sha256Digest";
  private String sha256Digest;

  public static final String JSON_PROPERTY_FILE_SIZE = "fileSize";
  private Long fileSize;

  public ObjectFile() { 
  }

  public ObjectFile filePath(String filePath) {
    
    this.filePath = filePath;
    return this;
  }

   /**
   * The object relative path of the file
   * @return filePath
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "path/to/file.txt", value = "The object relative path of the file")
  @JsonProperty(JSON_PROPERTY_FILE_PATH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getFilePath() {
    return filePath;
  }


  @JsonProperty(JSON_PROPERTY_FILE_PATH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }


  public ObjectFile sha256Digest(String sha256Digest) {
    
    this.sha256Digest = sha256Digest;
    return this;
  }

   /**
   * The sha256 digest of the file
   * @return sha256Digest
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1ff0a30eedd7819f4552179c94ea89ed03f5d1a4265fcd0f971ebe7bc17317cf", value = "The sha256 digest of the file")
  @JsonProperty(JSON_PROPERTY_SHA256_DIGEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getSha256Digest() {
    return sha256Digest;
  }


  @JsonProperty(JSON_PROPERTY_SHA256_DIGEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSha256Digest(String sha256Digest) {
    this.sha256Digest = sha256Digest;
  }


  public ObjectFile fileSize(Long fileSize) {
    
    this.fileSize = fileSize;
    return this;
  }

   /**
   * Size of the file in bytes
   * @return fileSize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1024", value = "Size of the file in bytes")
  @JsonProperty(JSON_PROPERTY_FILE_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getFileSize() {
    return fileSize;
  }


  @JsonProperty(JSON_PROPERTY_FILE_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ObjectFile objectFile = (ObjectFile) o;
    return Objects.equals(this.filePath, objectFile.filePath) &&
        Objects.equals(this.sha256Digest, objectFile.sha256Digest) &&
        Objects.equals(this.fileSize, objectFile.fileSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filePath, sha256Digest, fileSize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ObjectFile {\n");
    sb.append("    filePath: ").append(toIndentedString(filePath)).append("\n");
    sb.append("    sha256Digest: ").append(toIndentedString(sha256Digest)).append("\n");
    sb.append("    fileSize: ").append(toIndentedString(fileSize)).append("\n");
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

