package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
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
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Building> buildings;
  @OneToMany(mappedBy = "kingdom")
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Resource> resources;
  @OneToMany(mappedBy = "kingdom")
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Troop> troops;
  @OneToOne()
  private Location location;

  public Kingdom(int id, String name, Player player) {
    this.id = id;
    this.name = name;
    this.player = player;
  }

  public Kingdom(int id, String name, Player player,
                 List<Building> buildings, List<Resource> resources, Location location) {
    this.id = id;
    this.name = name;
    this.player = player;
    this.buildings = buildings;
    this.resources = resources;
    this.location = location;
  }

  public Kingdom(String name, Player player, Location location) {
    this.name = name;
    this.player = player;
    this.location = location;
  }

  public Kingdom(String name, Player player) {
    this.name = name;
    this.player = player;
  }

  public Kingdom(int id, Location location) {
    this.id = id;
    this.location = location;
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

  public void setBuildings(List<Building> buildings) {
    this.buildings = buildings;
  }

  public List<Troop> getTroops() {
    return troops;
  }

  public void setTroops(List<Troop> troops) {
    this.troops = troops;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public List<Troop> getTroops() {
    return troops;
  }

  public void setTroops(List<Troop> troops) {
    this.troops = troops;
  }
}
