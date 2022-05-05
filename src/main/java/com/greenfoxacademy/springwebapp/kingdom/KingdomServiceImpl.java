package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.exceptions.WrongIdException;
import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.dtos.KingdomPostDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.location.LocationService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KingdomServiceImpl implements KingdomService {

  private final KingdomRepository kingdomRepository;
  private final BuildingService buildingService;
  private final ResourceService resourceService;
  private final LocationService locationService;

  @Autowired
  public KingdomServiceImpl(
      KingdomRepository kingdomRepository,
      BuildingService buildingService,
      ResourceService resourceService,
      LocationService locationService) {
    this.kingdomRepository = kingdomRepository;
    this.buildingService = buildingService;
    this.resourceService = resourceService;
    this.locationService = locationService;
  }

  @Autowired
  private GameObjectRuleHolder gameObjectRuleHolder;

  @Override
  public Kingdom save(String kingdomName, Player player) {
    if (kingdomName == null || kingdomName.isEmpty()) {
      kingdomName = player.getUsername() + "'s kingdom";
    }
    Kingdom savedKingdom = kingdomRepository.save(new Kingdom(kingdomName, player,
        locationService.createLocation()));
    return defaultResourceCreator(defaultBuildingCreator(savedKingdom));
  }

  @Override
  public void checkInputParameters(KingdomPostDTO kingdomPostDTO) {
    if (kingdomPostDTO == null || kingdomPostDTO.getBuildingId() == null) {
      throw new RequestParameterMissingException("buildingId must be present");
    }
  }

  @Override
  public void checkResources(Building building, int level) throws NotEnoughResourceException {
    int goldResourcesNumber = building
            .getKingdom()
            .getResources()
            .stream()
            .filter(resource -> resource.getResourceType() == ResourceType.GOLD)
            .mapToInt(Resource::getAmount)
            .sum();
    int requiredCost = gameObjectRuleHolder.getBuildingCostMultiplier(BuildingType.ACADEMY.name(), level);
    if (requiredCost > goldResourcesNumber) {
      throw new NotEnoughResourceException();
    }
  }

  @Override
  public void checkBuildingId(Building building, Player player) throws WrongIdException {
    if (building.getKingdom().getPlayer().getId() != player.getId()) {
      throw new WrongIdException("Forbidden action");
    }
  }

  @Override
  public void checkBuildingType(Building building) throws RequestNotAcceptableException {
    if (building.getBuildingType() != BuildingType.ACADEMY) {
      throw new RequestNotAcceptableException("Not a valid academy id");
    }
  }

  @Override
  public void checkOwner(Building building, Integer kingdomId) {

  }

  private Kingdom defaultBuildingCreator(Kingdom kingdom) {
    LocalDateTime currentTimestamp = LocalDateTime.now();

    Building defaultTownhall =
        new Building(BuildingType.TOWNHALL, 1, kingdom, currentTimestamp, currentTimestamp);
    Building defaultMine =
        new Building(BuildingType.FARM, 1, kingdom, currentTimestamp, currentTimestamp);
    Building defaultFarm =
        new Building(BuildingType.MINE, 1, kingdom, currentTimestamp, currentTimestamp);
    Building defaultAcademy =
        new Building(BuildingType.ACADEMY, 1, kingdom, currentTimestamp, currentTimestamp);
    List<Building> initialBuildings = new ArrayList<>();
    Collections.addAll(initialBuildings, defaultTownhall, defaultMine, defaultFarm, defaultAcademy);
    buildingService.saveAll(initialBuildings);
    kingdom.setBuildings(initialBuildings);

    return kingdom;
  }

  private Kingdom defaultResourceCreator(Kingdom kingdom) {
    LocalDateTime currentTimestamp = LocalDateTime.now();
    int initialFoodAmount = 1000;
    int initialGoldAmount = 1000;
    Resource initialFood = new Resource(ResourceType.FOOD, initialFoodAmount, 0, currentTimestamp, kingdom);
    Resource initialGold = new Resource(ResourceType.GOLD, initialGoldAmount, 0, currentTimestamp, kingdom);
    List<Resource> initialResources = new ArrayList<>();
    Collections.addAll(initialResources, initialFood, initialGold);
    resourceService.saveAll(initialResources);
    kingdom.setResources(initialResources);

    return kingdom;
  }
}