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
import edu.wisc.library.sdg.preservation.manager.client.model.EventOutcomeCount;
import edu.wisc.library.sdg.preservation.manager.client.model.EventType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * EventCounts
 */
@JsonPropertyOrder({
  EventCounts.JSON_PROPERTY_EVENT_TYPE,
  EventCounts.JSON_PROPERTY_EVENT_COUNTS_BY_OUTCOME
})
@JsonTypeName("EventCounts")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class EventCounts {
  public static final String JSON_PROPERTY_EVENT_TYPE = "eventType";
  private EventType eventType;

  public static final String JSON_PROPERTY_EVENT_COUNTS_BY_OUTCOME = "eventCountsByOutcome";
  private EventOutcomeCount eventCountsByOutcome;

  public EventCounts() { 
  }

  public EventCounts eventType(EventType eventType) {
    
    this.eventType = eventType;
    return this;
  }

   /**
   * Get eventType
   * @return eventType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_EVENT_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventType getEventType() {
    return eventType;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventType(EventType eventType) {
    this.eventType = eventType;
  }


  public EventCounts eventCountsByOutcome(EventOutcomeCount eventCountsByOutcome) {
    
    this.eventCountsByOutcome = eventCountsByOutcome;
    return this;
  }

   /**
   * Get eventCountsByOutcome
   * @return eventCountsByOutcome
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_EVENT_COUNTS_BY_OUTCOME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventOutcomeCount getEventCountsByOutcome() {
    return eventCountsByOutcome;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_COUNTS_BY_OUTCOME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventCountsByOutcome(EventOutcomeCount eventCountsByOutcome) {
    this.eventCountsByOutcome = eventCountsByOutcome;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventCounts eventCounts = (EventCounts) o;
    return Objects.equals(this.eventType, eventCounts.eventType) &&
        Objects.equals(this.eventCountsByOutcome, eventCounts.eventCountsByOutcome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, eventCountsByOutcome);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventCounts {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    eventCountsByOutcome: ").append(toIndentedString(eventCountsByOutcome)).append("\n");
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

