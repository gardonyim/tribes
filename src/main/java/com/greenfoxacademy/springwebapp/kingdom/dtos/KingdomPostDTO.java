package com.greenfoxacademy.springwebapp.kingdom.dtos;

public class KingdomPostDTO {
  private Integer buildingId;

  public KingdomPostDTO() {
  }

  public KingdomPostDTO(Integer buildingId) {
    this.buildingId = buildingId;
  }

  public Integer getBuildingId() {
    return buildingId;
  }

  public void setBuildingId(Integer buildingId) {
    this.buildingId = buildingId;
  }
}
