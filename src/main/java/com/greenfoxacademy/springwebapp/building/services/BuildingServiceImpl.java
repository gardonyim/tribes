package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingsDTO;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestedResourceNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.KingdomService;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.utilities.TimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class BuildingServiceImpl implements BuildingService {

  private static final Set<BuildingType> generatorBuildingTypes = new HashSet<>(Arrays.asList(
      BuildingType.FARM, BuildingType.MINE
  ));

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
    resourceService.pay(kingdom, gameObjectRuleHolder.calcCreationCost(type, 0, 1));
    resourceService.updateResourceGeneration(kingdom, type, 0, 1);
    return convertToDTO(buildingRepository.save(building));
  }

  @Override
  public BuildingDTO provideDtoAboutBuildingDevResoult(BuildingDTO buildingDTO, Kingdom kingdom, Integer buildingId) {
    return convertToDTO(modifyBuildingLevel(buildingDTO, kingdom, buildingId));
  }

  @Override
  public Building modifyBuildingLevel(BuildingDTO buildingDTO, Kingdom kingdom, Integer buildingId) {
    Building modifiableBuilding = validateModifyBuildingLevelRequest(buildingDTO, kingdom, buildingId);
    int originalLevel = modifiableBuilding.getLevel();
    int reqBuildingLevel = (buildingDTO == null) ? originalLevel + 1 : buildingDTO.getLevel();
    String modifiableBuildingType = modifiableBuilding.getBuildingType().getName().toLowerCase();
    modifiableBuilding.setLevel(reqBuildingLevel);
    modifiableBuilding.setHp(gameObjectRuleHolder.calcNewHP(modifiableBuildingType, reqBuildingLevel));
    modifiableBuilding.setStartedAt(TimeService.actualTime());
    modifiableBuilding.setFinishedAt(TimeService.timeAtNSecondsAfterTimeStamp(gameObjectRuleHolder.calcCreationTime(
            modifiableBuildingType, modifiableBuilding.getLevel(), reqBuildingLevel),
        modifiableBuilding.getStartedAt()));
    int requiredGoldAmount = gameObjectRuleHolder.calcCreationCost(
            modifiableBuildingType, originalLevel, reqBuildingLevel);
    resourceService.pay(kingdom, requiredGoldAmount);
    if (generatorBuildingTypes.contains(modifiableBuilding.getBuildingType())) {
      resourceService.updateResourceGeneration(kingdom, modifiableBuildingType, originalLevel, reqBuildingLevel);
    }
    kingdomService.update(kingdom);
    return modifiableBuilding;
  }

  @Override
  public Building validateModifyBuildingLevelRequest(BuildingDTO buildingDTO, Kingdom kingdom, Integer buildingId) {
    if (buildingId == null) {
      throw new RequestParameterMissingException("Missing parameter(s): buildingId!");
    }
    Building modifiableBuilding = buildingRepository.findById(buildingId)
        .orElseThrow(() -> new RequestedResourceNotFoundException("Required building is not exist!"));
    if (modifiableBuilding.getKingdom().getId() != kingdom.getId()) {
      throw new ForbiddenActionException();
    }
    int reqBuildingLevel = (buildingDTO == null) ? modifiableBuilding.getLevel() + 1 : buildingDTO.getLevel();
    if (modifiableBuilding.getBuildingType() != BuildingType.TOWNHALL
        && findTownhall(kingdom).getLevel() < reqBuildingLevel) {
      throw new RequestNotAcceptableException("Cannot build buildings with higher level than the Townhall");
    }
    validateHasEnoughGold(modifiableBuilding, reqBuildingLevel);
    return modifiableBuilding;
  }

  public void validateHasEnoughGold(Building modifiableBuilding, int reqBuildingLevel) {
    if (!resourceService.hasEnoughGold(modifiableBuilding.getKingdom(),
        gameObjectRuleHolder.calcCreationCost(modifiableBuilding.getBuildingType().getName().toLowerCase(),
            modifiableBuilding.getLevel(), reqBuildingLevel))) {
      throw new NotEnoughResourceException();
    }
  }

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
    if (!resourceService.hasEnoughGold(kingdom, gameObjectRuleHolder.calcCreationCost(typeDTO.getType(), 0, 1))) {
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

  @Override
  public Building findTownhall(Kingdom kingdom) {
    return kingdom.getBuildings().stream().filter(b -> b.getBuildingType() == BuildingType.TOWNHALL).findFirst().get();
  }

}