package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.models.Resource;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;

import java.util.List;

@Entity(name = "kingdoms")
public class Kingdom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  @OneToOne()
  private Player player;
  @OneToMany(mappedBy = "kingdom")
  private List<Building> buildings;
  @OneToMany(mappedBy = "kingdom")
  private List<Resource> resources;

  public Kingdom(int id, String name, Player player) {
    this.id = id;
    this.name = name;
    this.player = player;
  }

  public Kingdom(String name, Player player) {
    this.name = name;
    this.player = player;
  }

  public Kingdom() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public List<Resource> getResources() {
    return resources;
  }

  public void setResources(List<Resource> resources) {
    this.resources = resources;
  }

  public List<Building> getBuildings() {
    return buildings;
  }

  public void setBuildings(
      List<Building> buildings) {
    this.buildings = buildings;
  }
}
