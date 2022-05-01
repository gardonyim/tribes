package com.greenfoxacademy.springwebapp.building.models;

public class BuildingTypeDTO {

  private String type;

  public BuildingTypeDTO(String type) {
    this.type = type;
  }

  public BuildingTypeDTO() {
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
