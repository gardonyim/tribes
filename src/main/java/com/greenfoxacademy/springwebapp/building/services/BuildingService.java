package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingsDTO;
import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import java.util.List;

public interface BuildingService {

  Building getBuildingById(Integer buildingId);

  Building saveBuilding(Building building);

  Iterable<Building> saveAll(List<Building> buildings);

  void checkOwner(Building building, Integer kingdomId) throws ForbiddenActionException;

  BuildingDTO addBuilding(BuildingTypeDTO typeDTO, Kingdom kingdom);

  BuildingsDTO getBuildingDtoList(Kingdom kingdom);

  BuildingDTO getBuildingDTO(Integer id, Kingdom kingdom);

  BuildingDTO convertToDTO(Building building);

  List<BuildingDTO> convertToDTOs(List<Building> buildings);

}