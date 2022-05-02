package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.exceptions.BuildingDoesNotBelongToPlayerException;
import com.greenfoxacademy.springwebapp.exceptions.BuildingTypeException;
import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
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
import com.greenfoxacademy.springwebapp.troop.TroopRepository;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KingdomServiceImpl implements KingdomService {

  private KingdomRepository kingdomRepository;
  private BuildingService buildingService;
  private ResourceService resourceService;
  private LocationService locationService;
  private TroopRepository troopRepository;

  @Autowired
  public KingdomServiceImpl(
      KingdomRepository kingdomRepository,
      BuildingService buildingService,
      ResourceService resourceService,
      LocationService locationService,
      TroopRepository troopRepository) {
    this.kingdomRepository = kingdomRepository;
    this.troopRepository = troopRepository;
    this.buildingService = buildingService;
    this.resourceService = resourceService;
    this.locationService = locationService;
  }

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
  public List<Troop> getTroopsOfKingdom(Integer kingdomId) {
    return troopRepository.findTroopsByKingdomId(kingdomId);
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
    int requiredGold = level == 1 ? 150 : level * 100;
    if (requiredGold > goldResourcesNumber) {
      throw new NotEnoughResourceException();
    }
  }

  @Override
  public void checkBuildingType(Building building) throws BuildingTypeException {
    if (building.getBuildingType() != BuildingType.ACADEMY) {
      throw new BuildingTypeException();
    }
  }

  @Override
  public void checkOwner(Building building, Integer kingdomId) throws BuildingDoesNotBelongToPlayerException {
    if (building.getKingdom().getId() != kingdomId) {
      throw new BuildingDoesNotBelongToPlayerException();
    }
  }

  @Override
  public void checkInputParameters(KingdomPostDTO kingdomPostDTO, String jwtToken) {
    if (kingdomPostDTO == null || kingdomPostDTO.getBuildingId() == null) {
      throw new RequestParameterMissingException("buildingId must be present");
    }

    if (jwtToken == null) {
      throw new RequestParameterMissingException("JWT token must be present.");
    }
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