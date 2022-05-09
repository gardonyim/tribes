package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;

import java.util.List;

public interface BuildingService {

  Building getBuildingById(Integer buildingId);

  Building saveBuilding(Building building);

  Iterable<Building> saveAll(List<Building> buildings);

  void checkOwner(Building building, Integer kingdomId) throws ForbiddenActionException;

  BuildingDTO addBuilding(BuildingTypeDTO typeDTO, Kingdom kingdom);

<<<<<<< HEAD
  BuildingDTO convertToDTO(Building building);

  List<BuildingDTO> convertToDTOs(List<Building> buildings);

  BuildingDTO modifyBuildingLevel(BuildingDTO buildingDTO, Kingdom kingdom, Integer buildingId);

  BuildingDTO modifyBuildingLevel(BuildingDTO buildingDTO, Kingdom kingdom, String buildingId);
=======
  BuildingDTO modifyBuildingLevel(BuildingDTO buildingDTO, Kingdom kingdom, Integer buildingId);
>>>>>>> 5d666ca (refactor(Put buildings): refactor methodes in buildingservice which are responsible for buildinglevel modification)

  Building validateModifyBuildingLevelRequest(BuildingDTO buildingDTO, Kingdom kingdom, Integer buildingId);

}