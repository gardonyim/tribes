package com.greenfoxacademy.springwebapp.building.models;

public enum BuildingType {
<<<<<<< HEAD
  TOWNHALL("townhall", 200),
  MINE("mine", 100),
  FARM("farm", 100),
  ACADEMY("academy", 150);

  private final String name;
  private final int hpParameter;

  private BuildingType(String name, int hpParameter) {
    this.name = name;
    this.hpParameter = hpParameter;
  }

  public String getName() {
    return name;
  }
<<<<<<< HEAD

  public int getHpParameter() {
    return hpParameter;
  }
=======
>>>>>>> f951a4494fd1c894b52377851e09ee94280121d1
}