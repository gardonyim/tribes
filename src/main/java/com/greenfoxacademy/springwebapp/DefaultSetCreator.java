package com.greenfoxacademy.springwebapp;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.building.services.BuildingServiceImpl;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultSetCreator {
  private BuildingService buildingService;
  private ResourceService resourceService;

  @Autowired
  public DefaultSetCreator(
      BuildingService buildingService,
      ResourceService resourceService) {
    this.buildingService = buildingService;
    this.resourceService = resourceService;
  }

  public void defaultSetup() {
    Kingdom kingdom = new Kingdom();
    LocalDateTime currentTimestamp = LocalDateTime.now();
    int initialFoodAmount = 1000;
    int initialGoldAmount = 1000;

    Building building1 = new Building(BuildingType.TOWNHALL, 1, kingdom, currentTimestamp,
        currentTimestamp);
    Building building2 = new Building(BuildingType.MINE, 1, kingdom, currentTimestamp,
        currentTimestamp);
    Building building3 = new Building(BuildingType.FARM, 1, kingdom, currentTimestamp,
        currentTimestamp);
    Building building4 = new Building(BuildingType.ACADEMY, 1, kingdom, currentTimestamp,
        currentTimestamp);
    Building savedBuilding1 = BuildingServiceImpl.saveBuilding(building1);
    Building savedBuilding2 = BuildingService.saveBuilding(building2);
    Building savedBuilding3 = BuildingService.saveBuilding(building3);
    Building savedBuilding4 = BuildingServiceImpl.saveBuilding(building4);
    List<Building> initialBuildings = new ArrayList<>();
    initialBuildings.add(savedBuilding1);
    initialBuildings.add(savedBuilding2);
    initialBuildings.add(savedBuilding3);
    initialBuildings.add(savedBuilding4);
    kingdom.setBuildings = initialBuildings;

    Resource initialFood = new Resource();
    Resource initialGold = new Resource();
    initialFood.setResourceType(ResourceType.FOOD);
    initialGold.setResourceType(ResourceType.GOLD);
    initialFood.setAmount(initialFoodAmount);
    initialGold.setAmount(initialGoldAmount);
    Resource savedInitialFood = resourceService.save(initialFood);
    Resource savedInitialGold = resourceService.save(initialGold);
    List<Resource> initialResources = new ArrayList<>();
    initialResources.add(savedInitialFood);
    initialResources.add(savedInitialGold);
    kingdom.setResources = initialResources;
  }
}