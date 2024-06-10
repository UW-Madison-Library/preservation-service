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
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The state of an Ingest Object
 */
public enum IngestObjectState {
  
  ANALYZING("ANALYZING"),
  
  ANALYSIS_FAILED("ANALYSIS_FAILED"),
  
  PENDING_REVIEW("PENDING_REVIEW"),
  
  PENDING_INGESTION("PENDING_INGESTION"),
  
  PENDING_REJECTION("PENDING_REJECTION"),
  
  INGESTING("INGESTING"),
  
  INGESTED("INGESTED"),
  
  INGEST_FAILED("INGEST_FAILED"),
  
  NO_CHANGE("NO_CHANGE"),
  
  REPLICATING("REPLICATING"),
  
  REPLICATED("REPLICATED"),
  
  REPLICATION_FAILED("REPLICATION_FAILED"),
  
  COMPLETE("COMPLETE"),
  
  REJECTING("REJECTING"),
  
  REJECTED("REJECTED"),
  
  DELETED("DELETED");

  private String value;

  IngestObjectState(String value) {
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
  public static IngestObjectState fromValue(String value) {
    for (IngestObjectState b : IngestObjectState.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

