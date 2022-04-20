package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TroopServiceImpl implements TroopService {
  private final TroopRepository troopRepository;

  @Autowired
  public TroopServiceImpl(TroopRepository troopRepository) {
    this.troopRepository = troopRepository;
  }

  @Override
  public List<Troop> findAllTroops() {
    return troopRepository.findAll();
  }

  @Override
  public void addNewTroop(Troop troop) {
    troopRepository.save(troop);
  }

  public Optional<Troop> getTroopById(Integer id) {
    return troopRepository.findById(id);
  }

  @Override
  public void upgradeTroop(Integer id, Integer buildingId) {
    //TODO: implement once Building entity is available
  }
}
