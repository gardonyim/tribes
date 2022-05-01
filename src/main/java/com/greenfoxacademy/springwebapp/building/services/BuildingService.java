package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.Building;
import java.util.List;

public interface BuildingService {

  Building saveBuilding(Building building);

  Iterable<Building> saveAll(List<Building> buildings);
}