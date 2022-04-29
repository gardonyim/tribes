package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.building.models.Building;
import java.util.List;
import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.resource.ResourceServiceImpl;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class BuildingServiceImpl implements BuildingService {

  private BuildingRepository buildingRepository;
  private ResourceService resourceService;

  @Autowired
  public BuildingServiceImpl(BuildingRepository buildingRepository,
                             ResourceServiceImpl resourceService) {
    this.buildingRepository = buildingRepository;
    this.resourceService = resourceService;
  }

  @Override
  public Building saveBuilding(Building building) {
    return buildingRepository.save(building);
  }

  @Override
  public Iterable<Building> saveAll(List<Building> buildings) {
    return buildingRepository.saveAll(buildings);
  }

  public BuildingDTO addBuilding(BuildingTypeDTO typeDTO, Kingdom kingdom) {
    validateAddBuildingRequest(typeDTO, kingdom);
    return new BuildingDTO(buildingRepository.save(constructBuilding(typeDTO, kingdom)));
  }

  private void validateAddBuildingRequest(BuildingTypeDTO typeDTO, Kingdom kingdom) {
    if (typeDTO == null || typeDTO.getType() == null || typeDTO.getType().trim().isEmpty()) {
      throw new RequestParameterMissingException("Missing parameter(s): type!");
    }
    if (!ObjectUtils.containsConstant(BuildingType.values(), typeDTO.getType())) {
      throw new RequestNotAcceptableException("Invalid building type");
    }
    if (typeDTO.getType().toUpperCase().equals("TOWNHALL")) {
      throw new RequestCauseConflictException("There must be only one Townhall in a kingdom");
    }
    if (buildingRepository.findFirstByBuildingTypeAndKingdom(BuildingType.TOWNHALL, kingdom)
        .get().getLevel() < 1) {
      throw new RequestNotAcceptableException(
          "Cannot build buildings with higher level than the Townhall");
    }
    int required = BuildingType.valueOf(typeDTO.getType().toUpperCase()).getHpParameter();  // TODO: change when Game settings merged
    int available = resourceService.getResourceByKingdomAndType(kingdom, ResourceType.GOLD).getAmount();
    if (available < required) {
      throw new RequestCauseConflictException("Not enough resource");
    }
  }

  private Building constructBuilding(BuildingTypeDTO typeDTO,
                                     Kingdom kingdom) {
    Building building = new Building();
    building.setBuildingType(BuildingType.valueOf(typeDTO.getType().toUpperCase()));
    building.setLevel(1);
    building.setHp(building.getBuildingType().getHpParameter());
    building.setKingdom(kingdom);
    building.setStartedAt(TimeService.actualTime());
    building.setFinishedAt(TimeService.timeAtNSecondsLater(60L));  // TODO: Change when Game settings merged
    resourceService.pay(kingdom, ResourceType.GOLD, 100); // TODO: Change price when Game settings merged
    return building;
  }

}