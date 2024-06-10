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
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * ObjectVersionReplicatedRequest
 */
@JsonPropertyOrder({
  ObjectVersionReplicatedRequest.JSON_PROPERTY_OBJECT_ID,
  ObjectVersionReplicatedRequest.JSON_PROPERTY_PERSISTENCE_VERSION,
  ObjectVersionReplicatedRequest.JSON_PROPERTY_DATA_STORE,
  ObjectVersionReplicatedRequest.JSON_PROPERTY_DATA_STORE_KEY,
  ObjectVersionReplicatedRequest.JSON_PROPERTY_SHA256_DIGEST
})
@JsonTypeName("ObjectVersionReplicatedRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class ObjectVersionReplicatedRequest {
  public static final String JSON_PROPERTY_OBJECT_ID = "objectId";
  private String objectId;

  public static final String JSON_PROPERTY_PERSISTENCE_VERSION = "persistenceVersion";
  private String persistenceVersion;

  public static final String JSON_PROPERTY_DATA_STORE = "dataStore";
  private DataStore dataStore;

  public static final String JSON_PROPERTY_DATA_STORE_KEY = "dataStoreKey";
  private String dataStoreKey;

  public static final String JSON_PROPERTY_SHA256_DIGEST = "sha256Digest";
  private String sha256Digest;

  public ObjectVersionReplicatedRequest() { 
  }

  public ObjectVersionReplicatedRequest objectId(String objectId) {
    
    this.objectId = objectId;
    return this;
  }

   /**
   * The internal ID of the object
   * @return objectId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "urn:uuid:3c658673-38aa-4425-b931-2971c45256d3", value = "The internal ID of the object")
  @JsonProperty(JSON_PROPERTY_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getObjectId() {
    return objectId;
  }


  @JsonProperty(JSON_PROPERTY_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }


  public ObjectVersionReplicatedRequest persistenceVersion(String persistenceVersion) {
    
    this.persistenceVersion = persistenceVersion;
    return this;
  }

   /**
   * The version identifier of the object version
   * @return persistenceVersion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "v2", value = "The version identifier of the object version")
  @JsonProperty(JSON_PROPERTY_PERSISTENCE_VERSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPersistenceVersion() {
    return persistenceVersion;
  }


  @JsonProperty(JSON_PROPERTY_PERSISTENCE_VERSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPersistenceVersion(String persistenceVersion) {
    this.persistenceVersion = persistenceVersion;
  }


  public ObjectVersionReplicatedRequest dataStore(DataStore dataStore) {
    
    this.dataStore = dataStore;
    return this;
  }

   /**
   * Get dataStore
   * @return dataStore
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_DATA_STORE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public DataStore getDataStore() {
    return dataStore;
  }


  @JsonProperty(JSON_PROPERTY_DATA_STORE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setDataStore(DataStore dataStore) {
    this.dataStore = dataStore;
  }


  public ObjectVersionReplicatedRequest dataStoreKey(String dataStoreKey) {
    
    this.dataStoreKey = dataStoreKey;
    return this;
  }

   /**
   * The key the version is stored at in the datastore
   * @return dataStoreKey
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "The key the version is stored at in the datastore")
  @JsonProperty(JSON_PROPERTY_DATA_STORE_KEY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getDataStoreKey() {
    return dataStoreKey;
  }


  @JsonProperty(JSON_PROPERTY_DATA_STORE_KEY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setDataStoreKey(String dataStoreKey) {
    this.dataStoreKey = dataStoreKey;
  }


  public ObjectVersionReplicatedRequest sha256Digest(String sha256Digest) {
    
    this.sha256Digest = sha256Digest;
    return this;
  }

   /**
   * The lowercase, hex encoded sha256 digest of the archive
   * @return sha256Digest
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "The lowercase, hex encoded sha256 digest of the archive")
  @JsonProperty(JSON_PROPERTY_SHA256_DIGEST)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getSha256Digest() {
    return sha256Digest;
  }


  @JsonProperty(JSON_PROPERTY_SHA256_DIGEST)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSha256Digest(String sha256Digest) {
    this.sha256Digest = sha256Digest;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ObjectVersionReplicatedRequest objectVersionReplicatedRequest = (ObjectVersionReplicatedRequest) o;
    return Objects.equals(this.objectId, objectVersionReplicatedRequest.objectId) &&
        Objects.equals(this.persistenceVersion, objectVersionReplicatedRequest.persistenceVersion) &&
        Objects.equals(this.dataStore, objectVersionReplicatedRequest.dataStore) &&
        Objects.equals(this.dataStoreKey, objectVersionReplicatedRequest.dataStoreKey) &&
        Objects.equals(this.sha256Digest, objectVersionReplicatedRequest.sha256Digest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectId, persistenceVersion, dataStore, dataStoreKey, sha256Digest);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ObjectVersionReplicatedRequest {\n");
    sb.append("    objectId: ").append(toIndentedString(objectId)).append("\n");
    sb.append("    persistenceVersion: ").append(toIndentedString(persistenceVersion)).append("\n");
    sb.append("    dataStore: ").append(toIndentedString(dataStore)).append("\n");
    sb.append("    dataStoreKey: ").append(toIndentedString(dataStoreKey)).append("\n");
    sb.append("    sha256Digest: ").append(toIndentedString(sha256Digest)).append("\n");
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
