package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.location.models.LocationDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.troop.models.TroopDTO;

import java.util.List;

public class KingdomResFullDTO extends KingdomBaseDTO {

  List<BuildingDTO> buildings;
  List<ResourceDTO> resources;
  List<TroopDTO> troops;

  public KingdomResFullDTO() {
  }

  public KingdomResFullDTO(int kingdomId, String name, int userId, LocationDTO location,
                           List<BuildingDTO> buildings, List<ResourceDTO> resources, List<TroopDTO> troops) {
    super(kingdomId, name, userId, location);
    this.buildings = buildings;
    this.resources = resources;
    this.troops = troops;
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
}
