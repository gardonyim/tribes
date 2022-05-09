package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestedResourceNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.KingdomService;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import org.springframework.util.ObjectUtils;

@Service
public class BuildingServiceImpl implements BuildingService {

  private BuildingRepository buildingRepository;
  private ResourceService resourceService;
  private KingdomService kingdomService;
  private GameObjectRuleHolder gameObjectRuleHolder;

  @Autowired
  public void setBuildingRepository(BuildingRepository buildingRepository) {
    this.buildingRepository = buildingRepository;
  }

  @Autowired
  public void setResourceService(ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @Autowired
  public void setKingdomService(KingdomService kingdomService) {
    this.kingdomService = kingdomService;
  }

  @Autowired
  public void setGameObjectRuleHolder(GameObjectRuleHolder gameObjectRuleHolder) {
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
  public BuildingDTO modifyBuildingLevel(BuildingDTO buildingDTO, Kingdom kingdom, String buildingId) {
    Building modifiableBuilding = validateModifyBuildingLevelRequest(buildingDTO, kingdom, buildingId);
    int reqBuildingLevel = (buildingDTO == null) ? modifiableBuilding.getLevel() + 1 : buildingDTO.getLevel();
    modifiableBuilding.setLevel(reqBuildingLevel);
    modifiableBuilding.setHp(reqBuildingLevel * gameObjectRuleHolder.getHpMultiplier(
        modifiableBuilding.getBuildingType().getName().toLowerCase(), reqBuildingLevel));
    int requiredGoldAmount = calcRequiredGoldAmount(modifiableBuilding, reqBuildingLevel);
    modifiableBuilding.setStartedAt(TimeService.actualTime());
    modifiableBuilding.setFinishedAt(TimeService.timeAtNSecondsAfterTimeStamp(
        gameObjectRuleHolder.getBuildingTimeMultiplier(
        modifiableBuilding.getBuildingType().getName().toLowerCase(), modifiableBuilding.getLevel())
            * modifiableBuilding.getLevel(), modifiableBuilding.getStartedAt()));
    Resource modifiableResource = kingdom.getResources().stream()
        .filter(r -> r.getResourceType().getDescription().equals("gold")).findFirst().get();
    modifiableResource.setAmount(modifiableResource.getAmount() - requiredGoldAmount);
    modifiableResource.setUpdatedAt(modifiableBuilding.getStartedAt());
    kingdomService.update(kingdom);
    return convertToDTO(modifiableBuilding);
  }

  @Override
  public Building validateModifyBuildingLevelRequest(BuildingDTO buildingDTO, Kingdom kingdom, String buildingId) {
    if (buildingId == null || !buildingId.trim().matches("\\d+")) {
      throw new RequestParameterMissingException("Missing parameter(s): buildingId!");
    }
    int reqBuildingId = Integer.valueOf(buildingId);
    List<Building> modifiableBuildings = kingdom.getBuildings().stream()
        .filter(b -> b.getId() == reqBuildingId).collect(Collectors.toList());
    if (modifiableBuildings.size() == 0) {
      throw new ForbiddenActionException();
    }
    Building modifiableBuilding = modifiableBuildings.get(0);
    int reqBuildingLevel = (buildingDTO == null) ? modifiableBuilding.getLevel() + 1 : buildingDTO.getLevel();
    if (!modifiableBuilding.getBuildingType().getName().equalsIgnoreCase("townhall")
        && kingdom.getBuildings().stream().filter(b -> b.getBuildingType().getName()
            .equalsIgnoreCase("townhall")).collect(Collectors.toList()).get(0)
            .getLevel() < reqBuildingLevel) {
      throw new RequestNotAcceptableException("Cannot build buildings with higher level than the Townhall");
    }
    validateReqResourceAmount(modifiableBuilding, reqBuildingLevel);
    return modifiableBuilding;
  }

  private void validateReqResourceAmount(Building modifiableBuilding, int reqBuildingLevel) {
    int requiredGoldAmount = calcRequiredGoldAmount(modifiableBuilding, reqBuildingLevel);
    int availableGoldAmount = modifiableBuilding.getKingdom().getResources().stream()
        .filter(r -> r.getResourceType().getDescription().equals("gold")).map(r -> r.getAmount()).findFirst().get();
    if (availableGoldAmount < requiredGoldAmount) {
      throw new RequestCauseConflictException("Not enough resources");
    }
  }

  private int calcRequiredGoldAmount(Building modifiableBuilding, int reqBuildingLevel) {
    return (modifiableBuilding.getLevel() >= reqBuildingLevel) ? 0
        : reqBuildingLevel * gameObjectRuleHolder.getBuildingCostMultiplier(
        modifiableBuilding.getBuildingType().getName().toLowerCase(), reqBuildingLevel);
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
    dto.setBuildingType(building.getBuildingType().name().toLowerCase());
    dto.setLevel(building.getLevel());
    dto.setHp(building.getHp());
    dto.setStartedAt(TimeService.toEpochSecond(building.getStartedAt()));
    dto.setFinishedAt(TimeService.toEpochSecond(building.getFinishedAt()));
    return dto;
  }

}