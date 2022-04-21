package com.greenfoxacademy.springwebapp.building.models;

public enum BuildingType {

  TOWNHALL("townhall",200),

  MINE("mine",100),

  FARM("farm",100),

  ACADEMY("academy",150);

  private final String name;
  private final int hpParameter;

  private BuildingType(String name, int hpParameter) {
    this.name = name;
    this.hpParameter = hpParameter;
  }

  public String getName() {
    return name;
  }

  public int getHpParameter() {
    return hpParameter;
  }
}