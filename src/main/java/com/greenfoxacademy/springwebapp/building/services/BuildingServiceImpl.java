package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
  public Optional<Building> getBuildingById(Integer buildingId) {
    return buildingRepository.findById(buildingId);
  }

  @Override
  public Iterable<Building> saveAll(List<Building> buildings) {
    return buildingRepository.saveAll(buildings);
  }

  @Override
  public void checkOwner(Building building, Integer kingdomId) throws RequestNotAcceptableException {
    if (building.getKingdom().getId() != kingdomId) {
      throw new RequestNotAcceptableException("Not a valid academy id");
    }
  }
}