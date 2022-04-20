package com.greenfoxacademy.springwebapp.troop.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PutTroopDto {
  @JsonProperty("buildingId")
  private Integer buildingId;

  public Integer getBuildingId() {
    return buildingId;
  }

  public void setBuildingId(Integer buildingId) {
    this.buildingId = buildingId;
  }
}
