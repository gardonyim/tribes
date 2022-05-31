package com.greenfoxacademy.springwebapp.battle;

import com.greenfoxacademy.springwebapp.battle.models.BattleDetails;
import com.greenfoxacademy.springwebapp.battle.models.BattleReqDTO;
import com.greenfoxacademy.springwebapp.battle.models.BattleResDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;

public interface BattleService {

  BattleResDTO battle(Kingdom attacker, Integer defenderId, BattleReqDTO reqDTO);

  BattleDetails battlePreparation(BattleDetails battleDetails);

  BattleDetails battleExecution(BattleDetails battleDetails);

  void battleTermination(BattleDetails battleDetails);

}
