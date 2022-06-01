package com.greenfoxacademy.springwebapp.battle;

import com.greenfoxacademy.springwebapp.battle.models.BattleDetails;
import com.greenfoxacademy.springwebapp.battle.models.BattleReqDTO;
import com.greenfoxacademy.springwebapp.battle.models.BattleResDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.springframework.stereotype.Service;

@Service
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
    for (int i = 0; i < 3; i++) {
      round(battleDetails);
      if (battleDetails.getAttackerClones().isEmpty() || battleDetails.getDefenderClones().isEmpty()) {
        return battleDetails;
      }
    }
    return battleDetails;
  }

  private void round(BattleDetails battleDetails) {
    int attackerNr = battleDetails.getAttackerClones().size();
    int defenderNr = battleDetails.getDefenderClones().size();
    for (int i = 0; i < Math.max(attackerNr, defenderNr); i++) {
      Troop attacker = battleDetails.getAttackerClones().get(i % attackerNr);
      Troop defender = battleDetails.getDefenderClones().get(i % defenderNr);
      strike(attacker, defender);
      if (checkHealth(defender, battleDetails)) {
        battleDetails.removeDefenderClone(defender);
      } else {
        strike(defender, attacker);
        if (checkHealth(attacker, battleDetails)) {
          battleDetails.removeAttackerClone(attacker);
        }
      }
      if (battleDetails.getAttackerClones().isEmpty() || battleDetails.getDefenderClones().isEmpty()) {
        return;
      }
    }
  }

  private void strike(Troop hitter, Troop target) {
    int strikeForce = Math.max(hitter.getAttack() - target.getDefence(), 0);
    target.setHp(target.getHp() - strikeForce);
  }

  private boolean checkHealth(Troop troop, BattleDetails battleDetails) {
    if (troop.getHp() <= 0) {
      return battleDetails.addFallenTroop(troop);
    }
    return false;
  }

  @Override
  public void battleTermination(BattleDetails battleDetails) {}

}
