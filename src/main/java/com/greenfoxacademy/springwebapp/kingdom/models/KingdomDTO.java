package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KingdomDTO {

  private int id;
  private String name;
  private int userId;
  private List<BuildingDTO> buildings = new ArrayList<>();
  private List<ResourceDTO> resources = new ArrayList<>();
  private List<TroopDTO> troops = new ArrayList<>();
  private Location location;

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

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public List<BuildingDTO> getBuildings() {
    return buildings;
  }

  public void setBuildings(List<BuildingDTO> buildings) {
    this.buildings = buildings;
  }

  public List<ResourceDTO> getResources() {
    return resources;
  }

  public void setResources(List<ResourceDTO> resources) {
    this.resources = resources;
  }

  public List<TroopDTO> getTroops() {
    return troops;
  }

  public void setTroops(List<TroopDTO> troops) {
    this.troops = troops;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof KingdomDTO)) {
      return false;
    }

    KingdomDTO that = (KingdomDTO) o;

    if (id != that.id) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (!Objects.equals(name, that.name)) {
      return false;
    }
    if (!buildings.equals(that.buildings)) {
      return false;
    }
    if (!resources.equals(that.resources)) {
      return false;
    }
    if (!troops.equals(that.troops)) {
      return false;
    }
    return Objects.equals(location, that.location);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + userId;
    result = 31 * result + buildings.hashCode();
    result = 31 * result + resources.hashCode();
    result = 31 * result + troops.hashCode();
    result = 31 * result + (location != null ? location.hashCode() : 0);
    return result;
  }
}
