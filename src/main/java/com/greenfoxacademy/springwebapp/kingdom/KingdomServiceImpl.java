package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomBaseDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.location.LocationService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.greenfoxacademy.springwebapp.troop.TroopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KingdomServiceImpl implements KingdomService {

  private KingdomRepository kingdomRepository;
  private BuildingService buildingService;
  private ResourceService resourceService;
  private LocationService locationService;
  private TroopService troopService;

  @Autowired
  public KingdomServiceImpl(
      KingdomRepository kingdomRepository,
      BuildingService buildingService,
      ResourceService resourceService,
      LocationService locationService,
      TroopService troopService) {
    this.kingdomRepository = kingdomRepository;
    this.buildingService = buildingService;
    this.resourceService = resourceService;
    this.locationService = locationService;
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

  @Override
  public KingdomResFullDTO convertToKingdomResFullDTO(Kingdom kingdom) {
    return new KingdomResFullDTO(
        kingdom.getId(),
        kingdom.getName(),
        kingdom.getPlayer().getId(),
        locationService.convertToLocationDTO(kingdom.getLocation()),
        kingdom.getBuildings().stream()
            .map(buildingService::convertToDTO).collect(Collectors.toList()),
        kingdom.getResources().stream()
            .map(resourceService::convertToResourceDTO).collect(Collectors.toList()),
        kingdom.getTroops().stream()
            .map(troopService::convertToTroopDTO).collect(Collectors.toList())
    );
  }

  @Override
  public KingdomResWrappedDTO convertToKingdomResWrappedDTO(Kingdom kingdom) {
    return new KingdomResWrappedDTO(
        new KingdomBaseDTO(
            kingdom.getId(),
            kingdom.getName(),
            kingdom.getPlayer().getId(),
            locationService.convertToLocationDTO(kingdom.getLocation())
        )
    );
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