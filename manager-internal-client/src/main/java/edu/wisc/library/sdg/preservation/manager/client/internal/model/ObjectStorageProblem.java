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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * ObjectStorageProblem
 */
@JsonPropertyOrder({
  ObjectStorageProblem.JSON_PROPERTY_DATA_STORE,
  ObjectStorageProblem.JSON_PROPERTY_PROBLEM,
  ObjectStorageProblem.JSON_PROPERTY_PERSISTENCE_VERSION,
  ObjectStorageProblem.JSON_PROPERTY_REPORTED_TIMESTAMP
})
@JsonTypeName("ObjectStorageProblem")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class ObjectStorageProblem {
  public static final String JSON_PROPERTY_DATA_STORE = "dataStore";
  private DataStore dataStore;

  public static final String JSON_PROPERTY_PROBLEM = "problem";
  private StorageProblemType problem;

  public static final String JSON_PROPERTY_PERSISTENCE_VERSION = "persistenceVersion";
  private String persistenceVersion;

  public static final String JSON_PROPERTY_REPORTED_TIMESTAMP = "reportedTimestamp";
  private OffsetDateTime reportedTimestamp;

  public ObjectStorageProblem() { 
  }

  public ObjectStorageProblem dataStore(DataStore dataStore) {
    
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


  public ObjectStorageProblem problem(StorageProblemType problem) {
    
    this.problem = problem;
    return this;
  }

   /**
   * Get problem
   * @return problem
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_PROBLEM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public StorageProblemType getProblem() {
    return problem;
  }


  @JsonProperty(JSON_PROPERTY_PROBLEM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setProblem(StorageProblemType problem) {
    this.problem = problem;
  }


  public ObjectStorageProblem persistenceVersion(String persistenceVersion) {
    
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


  public ObjectStorageProblem reportedTimestamp(OffsetDateTime reportedTimestamp) {
    
    this.reportedTimestamp = reportedTimestamp;
    return this;
  }

   /**
   * Timestamp the storage problem was identified
   * @return reportedTimestamp
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "Timestamp the storage problem was identified")
  @JsonProperty(JSON_PROPERTY_REPORTED_TIMESTAMP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public OffsetDateTime getReportedTimestamp() {
    return reportedTimestamp;
  }


  @JsonProperty(JSON_PROPERTY_REPORTED_TIMESTAMP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setReportedTimestamp(OffsetDateTime reportedTimestamp) {
    this.reportedTimestamp = reportedTimestamp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ObjectStorageProblem objectStorageProblem = (ObjectStorageProblem) o;
    return Objects.equals(this.dataStore, objectStorageProblem.dataStore) &&
        Objects.equals(this.problem, objectStorageProblem.problem) &&
        Objects.equals(this.persistenceVersion, objectStorageProblem.persistenceVersion) &&
        Objects.equals(this.reportedTimestamp, objectStorageProblem.reportedTimestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataStore, problem, persistenceVersion, reportedTimestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ObjectStorageProblem {\n");
    sb.append("    dataStore: ").append(toIndentedString(dataStore)).append("\n");
    sb.append("    problem: ").append(toIndentedString(problem)).append("\n");
    sb.append("    persistenceVersion: ").append(toIndentedString(persistenceVersion)).append("\n");
    sb.append("    reportedTimestamp: ").append(toIndentedString(reportedTimestamp)).append("\n");
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

