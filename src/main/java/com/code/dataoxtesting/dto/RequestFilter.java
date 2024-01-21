package com.code.dataoxtesting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Value;

/**
 * Body that is sent by the client to filter specific job requirements.
 */
@Value
@Schema(description = "Request payload for the example endpoint")
public class RequestFilter {

  @JsonProperty("job_functions")
  @Schema(description = "job function", example = "[\"IT\", \"Design\"]")
  String[] jobFunctions;

  @JsonProperty("searchable_locations")
  @Schema(description = "searchable_locations", example = "[\"United States\", \"United Kingdom\"]")
  String[] searchableLocations;

  @JsonProperty("organization.industry_tags")
  @Schema(description = "industry tags", example = "[\"Administrative Services\", \"Advertising\"]")
  String[] industryTags;

  @JsonProperty("organization.head_count")
  @Schema(description = "industry tags", example = "[\"1\", \"2\"]")
  String[] sizeOfOrganization;

  @JsonProperty("organization.stage")
  @Schema(description = "company stage", example = "[\"seed\", \"pre_seed\"]")
  String[] organizationStage;

  @JsonProperty("organization.topics.topics")
  @Schema(description = "company topics", example = "[\"Data Science\", \"Marketing\"]")
  String[] organizationTopics;

  @JsonProperty("organization.id")
  @Schema(description = "id (recommended not to use it, because we dont know inner id", example = "[]")
  String[] organizationId;

  @JsonProperty("postedAtSorting")
  @Schema(description = "sorting by posted at date (default is desc, any other would be asc)", example = "any")
  String postedAtSorting;


  public Map<String, Object> toMap() {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(this);
      return objectMapper.readValue(jsonString, Map.class);
    } catch (Exception e) {
      throw new RuntimeException("Error converting RequestFilter to Map", e);
    }
  }
}
