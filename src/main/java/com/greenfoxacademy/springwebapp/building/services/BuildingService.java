package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.Building;
import java.util.List;

import java.util.Optional;

public interface BuildingService {

  public Optional<Building> getBuildingById(Integer buildingId);

  Building saveBuilding(Building building);

  Iterable<Building> saveAll(List<Building> buildings);

  public void checkOwner(Building building, Integer kingdomId);
}