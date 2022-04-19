package com.greenfoxacademy.springwebapp.building.models;

public enum BuildingType {
  TOWNHALL("townhall"), MINE("mine"), FARM("farm"), ACADEMY("academy");

  private String name;

  private BuildingType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}