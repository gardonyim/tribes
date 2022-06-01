package com.greenfoxacademy.springwebapp.building.models;

import java.util.List;

public class BuildingsDTO {
  private List<BuildingDTO> buildings;

  public BuildingsDTO() {
  }

  public BuildingsDTO(
      List<BuildingDTO> buildings) {
    this.buildings = buildings;
  }

  public List<BuildingDTO> getBuildings() {
    return buildings;
  }

  public void setBuildings(
      List<BuildingDTO> buildings) {
    this.buildings = buildings;
  }
}