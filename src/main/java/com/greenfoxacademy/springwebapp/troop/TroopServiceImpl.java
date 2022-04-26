package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TroopServiceImpl implements TroopService {

  private final TroopRepository troopRepository;

  @Autowired
  public TroopServiceImpl(TroopRepository troopRepository) {
    this.troopRepository = troopRepository;
  }

  @Override
  public Troop addNewTroop(Troop troop) {
    return troopRepository.save(troop);
  }
}
