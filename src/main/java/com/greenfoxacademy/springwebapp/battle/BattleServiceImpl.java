package com.greenfoxacademy.springwebapp.battle;

import com.greenfoxacademy.springwebapp.battle.models.BattleDetails;
import com.greenfoxacademy.springwebapp.battle.models.BattleReqDTO;
import com.greenfoxacademy.springwebapp.battle.models.BattleResDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;

public class BattleServiceImpl implements BattleService {

  @Override
  public BattleResDTO battle(Kingdom attacker, Integer defenderId, BattleReqDTO reqDTO) {
    return null;
  }

  @Override
  public BattleDetails battlePreparation(BattleDetails battleDetails) {
    return null;
  }

  @Override
  public BattleDetails battleExecution(BattleDetails battleDetails) {
    return null;
  }

  @Override
  public void battleTermination(BattleDetails battleDetails) {}

}
