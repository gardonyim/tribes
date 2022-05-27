package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingsDTO;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestedResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.resource.ResourceServiceImpl;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import java.util.Optional;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class BuildingServiceImpl implements BuildingService {

  private BuildingRepository buildingRepository;
  private ResourceService resourceService;
  private GameObjectRuleHolder gameObjectRuleHolder;

  @Autowired
  public BuildingServiceImpl(BuildingRepository buildingRepository,
                             ResourceServiceImpl resourceService,
                             GameObjectRuleHolder gameObjectRuleHolder) {
    this.buildingRepository = buildingRepository;
    this.resourceService = resourceService;
    this.gameObjectRuleHolder = gameObjectRuleHolder;
  }

  @Override
  public Building saveBuilding(Building building) {
    return buildingRepository.save(building);
  }

  @Override
  public Building getBuildingById(Integer buildingId) {
    return buildingRepository.findById(buildingId)
            .orElseThrow(() -> new RequestedResourceNotFoundException("No such building."));
  }

  @Override
  public Iterable<Building> saveAll(List<Building> buildings) {
    return buildingRepository.saveAll(buildings);
  }

  @Override
  public void checkOwner(Building building, Integer kingdomId) throws ForbiddenActionException {
    if (building.getKingdom().getId() != kingdomId) {
      throw new ForbiddenActionException();
    }
  }

  public BuildingDTO addBuilding(BuildingTypeDTO typeDTO, Kingdom kingdom) {
    validateAddBuildingRequest(typeDTO, kingdom);
    String type = typeDTO.getType();
    Building building = constructBuilding(type, 1, kingdom);
    resourceService.pay(kingdom, gameObjectRuleHolder.getBuildingCostMultiplier(type, 1));
    return convertToDTO(buildingRepository.save(building));
  }

  @Override
  public BuildingsDTO getBuildingDtoList(Kingdom kingdom) {
    return new BuildingsDTO(kingdom.getBuildings()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList()));
  }

  @Override
  public BuildingDTO getBuildingDTO(Integer id, Kingdom kingdom) {
    Optional<Building> building = buildingRepository.findById(id);
    if (building.isPresent()) {
      if (building.get().getKingdom().getId() == kingdom.getId()) {
        return convertToDTO(building.get());
      } else {
        throw new ForbiddenActionException();
      }
    } else {
      throw new RequestedResourceNotFoundException("Id not found");
    }
  }

  public void validateAddBuildingRequest(BuildingTypeDTO typeDTO, Kingdom kingdom) {
    if (typeDTO.getType() == null || typeDTO.getType().trim().isEmpty()) {
      throw new RequestParameterMissingException("Missing parameter(s): type!");
    }
    if (!ObjectUtils.containsConstant(BuildingType.values(), typeDTO.getType())) {
      throw new RequestNotAcceptableException("Invalid building type");
    }
    if (typeDTO.getType().equalsIgnoreCase("TOWNHALL")) {
      throw new RequestCauseConflictException("There must only be one Townhall in a kingdom");
    }
    if (buildingRepository.findFirstByBuildingTypeAndKingdom(BuildingType.TOWNHALL, kingdom)
            .get().getLevel() < 1) {
      throw new RequestNotAcceptableException("Cannot build buildings with higher level than the Townhall");
    }
    int requiredGoldAmount = gameObjectRuleHolder.getBuildingCostMultiplier(typeDTO.getType(), 1);
    int availableGoldAmount = resourceService.getResourceByKingdomAndType(kingdom, ResourceType.GOLD).getAmount();
    if (availableGoldAmount < requiredGoldAmount) {
      throw new RequestCauseConflictException("Not enough resources");
    }
  }

  public Building constructBuilding(String type, int level, Kingdom kingdom) {
    Building building = new Building();
    building.setBuildingType(BuildingType.valueOf(type.toUpperCase()));
    building.setLevel(level);
    building.setHp(gameObjectRuleHolder.getHpMultiplier(type, level));
    building.setKingdom(kingdom);
    building.setStartedAt(TimeService.actualTime());
    building.setFinishedAt(TimeService.timeAtNSecondsLater(
            gameObjectRuleHolder.getBuildingTimeMultiplier(type, level)));
    return building;
  }

  public BuildingDTO convertToDTO(Building building) {
    BuildingDTO dto = new BuildingDTO();
    dto.setId(building.getId());
    dto.setType(building.getBuildingType().name().toLowerCase());
    dto.setLevel(building.getLevel());
    dto.setHp(building.getHp());
    dto.setStartedAt(TimeService.toEpochSecond(building.getStartedAt()));
    dto.setFinishedAt(TimeService.toEpochSecond(building.getFinishedAt()));
    return dto;
  }

  @Override
  public List<BuildingDTO> convertToDTOs(List<Building> buildings) {
    return buildings.stream().map(this::convertToDTO).collect(Collectors.toList());
  }

}