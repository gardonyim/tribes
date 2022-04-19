package com.greenfoxacademy.springwebapp.building.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "buildings")
public class Building {
  @Id
  private int id;
  private BuildingType buildingType;
  private int level;
  private int hp;
  private int finishedAt;
}