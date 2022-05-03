package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.TroopDTO;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
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

  @Override
  public TroopDTO convertToTroopDTO(Troop troop) {
    return new TroopDTO(
        troop.getId(),
        troop.getLevel(),
        troop.getHp(),
        troop.getAttack(),
        troop.getDefence(),
        TimeService.toEpochSecond(troop.getStartedAt()),
        TimeService.toEpochSecond(troop.getFinishedAt())
    );
  }


}
