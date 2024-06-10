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
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.VersionStorageProblem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * SetObjectStorageProblemsRequest
 */
@JsonPropertyOrder({
  SetObjectStorageProblemsRequest.JSON_PROPERTY_OBJECT_ID,
  SetObjectStorageProblemsRequest.JSON_PROPERTY_DATA_STORE,
  SetObjectStorageProblemsRequest.JSON_PROPERTY_OBJECT_PROBLEM,
  SetObjectStorageProblemsRequest.JSON_PROPERTY_VERSION_PROBLEMS
})
@JsonTypeName("SetObjectStorageProblemsRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class SetObjectStorageProblemsRequest {
  public static final String JSON_PROPERTY_OBJECT_ID = "objectId";
  private String objectId;

  public static final String JSON_PROPERTY_DATA_STORE = "dataStore";
  private DataStore dataStore;

  public static final String JSON_PROPERTY_OBJECT_PROBLEM = "objectProblem";
  private StorageProblemType objectProblem;

  public static final String JSON_PROPERTY_VERSION_PROBLEMS = "versionProblems";
  private List<VersionStorageProblem> versionProblems = null;

  public SetObjectStorageProblemsRequest() { 
  }

  public SetObjectStorageProblemsRequest objectId(String objectId) {
    
    this.objectId = objectId;
    return this;
  }

   /**
   * The internal ID of the object
   * @return objectId
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "urn:uuid:3c658673-38aa-4425-b931-2971c45256d3", required = true, value = "The internal ID of the object")
  @JsonProperty(JSON_PROPERTY_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getObjectId() {
    return objectId;
  }


  @JsonProperty(JSON_PROPERTY_OBJECT_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }


  public SetObjectStorageProblemsRequest dataStore(DataStore dataStore) {
    
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


  public SetObjectStorageProblemsRequest objectProblem(StorageProblemType objectProblem) {
    
    this.objectProblem = objectProblem;
    return this;
  }

   /**
   * Get objectProblem
   * @return objectProblem
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_OBJECT_PROBLEM)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public StorageProblemType getObjectProblem() {
    return objectProblem;
  }


  @JsonProperty(JSON_PROPERTY_OBJECT_PROBLEM)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setObjectProblem(StorageProblemType objectProblem) {
    this.objectProblem = objectProblem;
  }


  public SetObjectStorageProblemsRequest versionProblems(List<VersionStorageProblem> versionProblems) {
    
    this.versionProblems = versionProblems;
    return this;
  }

  public SetObjectStorageProblemsRequest addVersionProblemsItem(VersionStorageProblem versionProblemsItem) {
    if (this.versionProblems == null) {
      this.versionProblems = new ArrayList<>();
    }
    this.versionProblems.add(versionProblemsItem);
    return this;
  }

   /**
   * Storage problems for specific versions
   * @return versionProblems
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Storage problems for specific versions")
  @JsonProperty(JSON_PROPERTY_VERSION_PROBLEMS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<VersionStorageProblem> getVersionProblems() {
    return versionProblems;
  }


  @JsonProperty(JSON_PROPERTY_VERSION_PROBLEMS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setVersionProblems(List<VersionStorageProblem> versionProblems) {
    this.versionProblems = versionProblems;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetObjectStorageProblemsRequest setObjectStorageProblemsRequest = (SetObjectStorageProblemsRequest) o;
    return Objects.equals(this.objectId, setObjectStorageProblemsRequest.objectId) &&
        Objects.equals(this.dataStore, setObjectStorageProblemsRequest.dataStore) &&
        Objects.equals(this.objectProblem, setObjectStorageProblemsRequest.objectProblem) &&
        Objects.equals(this.versionProblems, setObjectStorageProblemsRequest.versionProblems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectId, dataStore, objectProblem, versionProblems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetObjectStorageProblemsRequest {\n");
    sb.append("    objectId: ").append(toIndentedString(objectId)).append("\n");
    sb.append("    dataStore: ").append(toIndentedString(dataStore)).append("\n");
    sb.append("    objectProblem: ").append(toIndentedString(objectProblem)).append("\n");
    sb.append("    versionProblems: ").append(toIndentedString(versionProblems)).append("\n");
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

