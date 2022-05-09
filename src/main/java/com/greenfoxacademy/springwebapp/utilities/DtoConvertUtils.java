package com.greenfoxacademy.springwebapp.utilities;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.location.models.LocationDTO;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DtoConvertUtils {

  public static LocationDTO convertLocation(Location location) {
    LocationDTO dto = new LocationDTO(location.getxcoordinate(), location.getycoordinate());
    return dto;
  }

  public static BuildingDTO convertBuilding(Building building) {
    BuildingDTO dto = new BuildingDTO(
            building.getId(),
            building.getBuildingType(),
            building.getLevel(),
            building.getHp(),
            TimeService.toEpochSecond(building.getStartedAt()),
            TimeService.toEpochSecond(building.getFinishedAt()));
    return dto;
  }

  public static ResourceDTO convertResource(Resource resource) {
    ResourceDTO dto = new ResourceDTO(
            resource.getResourceType(),
            resource.getAmount(),
            resource.getGeneration(),
            TimeService.toEpochSecond(resource.getUpdatedAt()));
    return dto;
  }

  public static List<BuildingDTO> convertBuildings(List<Building> buildingList) {
    if (buildingList == null) {
      return new ArrayList<>();
    }
    return buildingList.stream().map(DtoConvertUtils::convertBuilding).collect(Collectors.toList());
  }

  public static List<ResourceDTO> convertResources(List<Resource> resourceList) {
    if (resourceList == null) {
      return new ArrayList<>();
    }
    return resourceList.stream().map(DtoConvertUtils::convertResource).collect(Collectors.toList());
  }

  public static List<TroopDTO> convertTroops(List<Troop> troopList) {
    if (troopList == null) {
      return new ArrayList<>();
    }
    return troopList.stream().map(DtoConvertUtils::convertTroop).collect(Collectors.toList());
  }

  public static TroopDTO convertTroop(Troop troop) {
    TroopDTO dto = new TroopDTO();
    dto.setId(troop.getId());
    dto.setLevel(troop.getLevel());
    dto.setHp(troop.getHp());
    dto.setAttack(troop.getAttack());
    dto.setDefence(troop.getDefence());
    dto.setStartedAt(TimeService.toEpochSecond(troop.getStartedAt()));
    dto.setFinishedAt(TimeService.toEpochSecond(troop.getFinishedAt()));
    return dto;
  }

}
