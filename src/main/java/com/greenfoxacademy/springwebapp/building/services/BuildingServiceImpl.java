package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.building.models.Building;

import java.util.List;

import com.greenfoxacademy.springwebapp.utilities.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {

  @Autowired
  private BuildingRepository buildingRepository;

  public BuildingServiceImpl(BuildingRepository buildingRepository) {
    this.buildingRepository = buildingRepository;
  }

  @Override
  public Building saveBuilding(Building building) {
    return buildingRepository.save(building);
  }

  @Override
  public Iterable<Building> saveAll(List<Building> buildings) {
    return buildingRepository.saveAll(buildings);
  }

  @Override
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