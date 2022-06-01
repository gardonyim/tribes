package com.greenfoxacademy.springwebapp.building.repositories;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface BuildingRepository extends CrudRepository<Building, Integer> {

  Optional<Building> findFirstByBuildingTypeAndKingdom(BuildingType type, Kingdom kingdom);

  Optional<Building> findById(Integer id);

  List<Building> findAllByKingdom(Kingdom kingdom);

}