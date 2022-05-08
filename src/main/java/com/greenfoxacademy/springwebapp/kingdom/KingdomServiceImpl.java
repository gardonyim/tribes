package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.exceptions.RequestedResourceNotFoundException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomBaseDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomPutDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.location.LocationService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.troop.TroopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KingdomServiceImpl implements KingdomService {

  private final KingdomRepository kingdomRepository;
  private final BuildingService buildingService;
  private final ResourceService resourceService;
  private final LocationService locationService;
  private final TroopService troopService;
  private final GameObjectRuleHolder gameObjectRuleHolder;

  @Autowired
  public KingdomServiceImpl(
          KingdomRepository kingdomRepository,
          BuildingService buildingService,
          ResourceService resourceService,
          LocationService locationService,
          GameObjectRuleHolder gameObjectRuleHolder,
          TroopService troopService) {
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

  @Override
  public Kingdom findById(Integer kingdomId) {
    return kingdomRepository.findById(kingdomId)
            .orElseThrow(() -> new RequestedResourceNotFoundException("The requested kingdom is not exist!"));
  }

  @Override
  public KingdomResFullDTO fetchKingdomData(Kingdom kingdom) {
    return convertToKingdomResFullDTO(kingdom);
  }

  @Override
  public KingdomResWrappedDTO fetchKingdomData(Integer kingdomId) {
    return convertToKingdomResWrappedDTO(findById(kingdomId));
  }

  @Override
  public KingdomResFullDTO convertToKingdomResFullDTO(Kingdom kingdom) {
    return new KingdomResFullDTO(
            kingdom.getId(),
            kingdom.getName(),
            kingdom.getPlayer().getId(),
            locationService.convertToLocationDTO(kingdom.getLocation()),
            buildingService.convertToDTOs(kingdom.getBuildings()),
            resourceService.convertToResourceDTOs(kingdom.getResources()),
            troopService.convert(kingdom.getTroops())
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

  public Kingdom update(Kingdom kingdom) {
    return kingdomRepository.save(kingdom);
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
  public KingdomResFullDTO renameKingdom(Kingdom kingdom, KingdomPutDTO kingdomPutDTO, Authentication auth) {
    checkKingdomPutDto(kingdomPutDTO);
    kingdom.setName(kingdomPutDTO.getName());
    Kingdom k = kingdomRepository.save(kingdom);
    return convertToKingdomResFullDTO(k);
  }
}