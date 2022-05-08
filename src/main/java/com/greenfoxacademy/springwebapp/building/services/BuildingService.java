package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingsDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import java.util.List;

public interface BuildingService {

  Building saveBuilding(Building building);

  Iterable<Building> saveAll(List<Building> buildings);

  BuildingDTO addBuilding(BuildingTypeDTO typeDTO, Kingdom kingdom);

  BuildingsDTO getBuildingDtoList(Kingdom kingdom);

  BuildingDTO getBuildingDTO(Integer id, Kingdom kingdom);
}