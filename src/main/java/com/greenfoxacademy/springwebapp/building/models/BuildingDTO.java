package com.greenfoxacademy.springwebapp.building.models;

import com.greenfoxacademy.springwebapp.utilities.TimeService;

public class BuildingDTO {

  private int id;
  private BuildingType buildingType;
  private int level;
  private int hp;
  private long startedAt;
  private long finishedAt;

  public BuildingDTO(Building building) {
    id = building.getId();
    buildingType = building.getBuildingType();
    level = building.getLevel();
    hp = building.getHp();
    startedAt = TimeService.toEpochSecond(building.getStartedAt());
    finishedAt = TimeService.toEpochSecond(building.getFinishedAt());
  }

  public BuildingDTO(int id,
                     BuildingType buildingType, int level, int hp, long startedAt,
                     long finishedAt) {
    this.id = id;
    this.buildingType = buildingType;
    this.level = level;
    this.hp = hp;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public BuildingType getBuildingType() {
    return buildingType;
  }

  public void setBuildingType(BuildingType buildingType) {
    this.buildingType = buildingType;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public long getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(long startedAt) {
    this.startedAt = startedAt;
  }

  public long getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(long finishedAt) {
    this.finishedAt = finishedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuildingDTO that = (BuildingDTO) o;
    if (id != that.id || level != that.level || hp != that.hp || startedAt != that.startedAt
        || finishedAt != that.finishedAt) {
      return false;
    }
    return buildingType == that.buildingType;
  }

}
