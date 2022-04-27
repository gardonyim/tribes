package com.greenfoxacademy.springwebapp.gamesettings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "game_rules")
public class GameObjectRule {

  @Id
  private int id;
  private String gameObjectType;
  @Column(name = "building_time_multiplier_1_in_sec")
  private int buildingTimeMultiplier1InSec;
  @Column(name = "building_time_multiplier_n_in_sec")
  private int buildingTimeMultiplierNInSec;
  @Column(name = "building_cost_multiplier_1")
  private int buildingCostMultiplier1;
  @Column(name = "building_cost_multiplier_n")
  private int buildingCostMultiplierN;
  private int hpMultiplier;

  public GameObjectRule() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getGameObjectType() {
    return gameObjectType;
  }

  public void setGameObjectType(String gameObjetType) {
    this.gameObjectType = gameObjetType;
  }

  public int getBuildingTimeMultiplier1InSec() {
    return buildingTimeMultiplier1InSec;
  }

  public void setBuildingTimeMultiplier1InSec(int buildingTimeMultiplier1) {
    this.buildingTimeMultiplier1InSec = buildingTimeMultiplier1;
  }

  public int getBuildingTimeMultiplierNInSec() {
    return buildingTimeMultiplierNInSec;
  }

  public void setBuildingTimeMultiplierNInSec(int buildingTimeMultiplierN) {
    this.buildingTimeMultiplierNInSec = buildingTimeMultiplierN;
  }

  public int getBuildingCostMultiplier1() {
    return buildingCostMultiplier1;
  }

  public void setBuildingCostMultiplier1(int buildingCostMultiplier1) {
    this.buildingCostMultiplier1 = buildingCostMultiplier1;
  }

  public int getBuildingCostMultiplierN() {
    return buildingCostMultiplierN;
  }

  public void setBuildingCostMultiplierN(int buildingCostMultiplierN) {
    this.buildingCostMultiplierN = buildingCostMultiplierN;
  }

  public int getHpMultiplier() {
    return hpMultiplier;
  }

  public void setHpMultiplier(int hpMultiplier) {
    this.hpMultiplier = hpMultiplier;
  }
}
