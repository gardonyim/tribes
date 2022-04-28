package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.Building;
import java.util.List;

public interface BuildingService {

  public Building saveBuilding(Building building);

  public List<Building> saveAll(List<Building> buildings);
}