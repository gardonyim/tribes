package com.greenfoxacademy.springwebapp.building.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "buildings")
public class Building {
  @Id
  private int id;
  private BuildingType buildingType;
  private int level;
  private int hp;
  private int finishedAt;

  public Building(BuildingType buildingType, int level, int hp, int finishedAt) {
    this.buildingType = buildingType;
    this.level = level;
    this.hp = hp;
    this.finishedAt = finishedAt;
  }

  public Building() {
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

  public void setBuildingType(BuildingType type) {
    this.buildingType = type;
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

  public int getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(int finishedAt) {
    this.finishedAt = finishedAt;
  }
}