package com.greenfoxacademy.springwebapp.troop.models;

public class TroopPostDTO {
  private Integer buildingId;

  public TroopPostDTO() {
  }

  public TroopPostDTO(Integer buildingId) {
    this.buildingId = buildingId;
  }

  public Integer getBuildingId() {
    return buildingId;
  }

  public void setBuildingId(Integer buildingId) {
    this.buildingId = buildingId;
  }
}
