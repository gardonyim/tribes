package com.greenfoxacademy.springwebapp.battle.models;

import java.util.List;

public class BattleReqDTO {

  private List<Integer> troopIds;

  public List<Integer> getTroopIds() {
    return troopIds;
  }

  public void setTroopIds(List<Integer> troopIds) {
    this.troopIds = troopIds;
  }
}
