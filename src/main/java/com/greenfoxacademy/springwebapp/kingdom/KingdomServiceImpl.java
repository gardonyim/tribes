package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomPutDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.location.LocationService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.troop.TroopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KingdomServiceImpl implements KingdomService {

  private final KingdomRepository kingdomRepository;
  private final BuildingService buildingService;
  private final ResourceService resourceService;
  private final LocationService locationService;
  private final GameObjectRuleHolder gameObjectRuleHolder;
  private final TroopServiceImpl troopService;

  @Autowired
  public KingdomServiceImpl(
          KingdomRepository kingdomRepository,
          BuildingService buildingService,
          ResourceService resourceService,
          LocationService locationService, GameObjectRuleHolder gameObjectRuleHolder, TroopServiceImpl troopService) {
    this.kingdomRepository = kingdomRepository;
    this.buildingService = buildingService;
    this.resourceService = resourceService;
    this.locationService = locationService;
    this.gameObjectRuleHolder = gameObjectRuleHolder;
    this.troopService = troopService;
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

  @Override
  public void checkKingdomPutDto(KingdomPutDTO kingdomPutDTO) {
    if (kingdomPutDTO == null || !StringUtils.hasText(kingdomPutDTO.getName())) {
      throw new RequestParameterMissingException("name is required.");
    }
  }

  @Override
  public KingdomResFullDTO renameKingdom(Kingdom kingdom, String newKingdomName) {
    kingdom.setName(newKingdomName);
    Kingdom k = kingdomRepository.save(kingdom);
    return convertToKingdomResFullDTO(k);
  }

  @Override
  public KingdomResFullDTO convertToKingdomResFullDTO(Kingdom kingdom) {
    KingdomResFullDTO kingdomResFullDTO = new KingdomResFullDTO();

    kingdomResFullDTO.setKingdomId(kingdom.getId());
    kingdomResFullDTO.setName(kingdom.getName());
    kingdomResFullDTO.setUserId(kingdom.getPlayer().getId());
    kingdomResFullDTO.setLocation(locationService.convertToLocationDTO(kingdom.getLocation()));
    kingdomResFullDTO.setBuildings(kingdom.getBuildings().stream().map(buildingService::convertToDTO).collect(Collectors.toList()));
    kingdomResFullDTO.setResources(kingdom.getResources().stream().map(resourceService::convertToResourceDTO).collect(Collectors.toList()));
    kingdomResFullDTO.setTroops(kingdom.getTroops().stream().map(troopService::convert).collect(Collectors.toList()));
    return kingdomResFullDTO;
  }
}