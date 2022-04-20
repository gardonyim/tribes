package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.troop.models.Troop;

import java.util.List;
import java.util.Optional;

public interface TroopService {

  List<Troop> findAllTroops();

  void addNewTroop(Troop troop);

  Optional<Troop> getTroopById(Integer id);

  void upgradeTroop(Integer id, Integer buildingId);
}
