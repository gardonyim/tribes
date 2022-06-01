package com.greenfoxacademy.springwebapp.battle.models;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.troop.models.Troop;

import java.util.ArrayList;
import java.util.List;

public class BattleDetails {

  private Kingdom attacker;
  private Kingdom defender;
  private List<Troop> attackerClones;
  private List<Troop> defenderClones;
  private int distance;
  private int townhallLevel;
  private List<Troop> fallenTroops;

  public BattleDetails(Kingdom attacker, Kingdom defender, List<Troop> attackerClones, List<Troop> defenderClones,
                     int distance, int townhallLevel) {
    this.attacker = attacker;
    this.defender = defender;
    this.attackerClones = attackerClones;
    this.defenderClones = defenderClones;
    this.distance = distance;
    this.townhallLevel = townhallLevel;
    this.fallenTroops = new ArrayList<>();
  }

  public boolean addFallenTroop(Troop fallenTroop) {
    return this.fallenTroops.add(fallenTroop);
  }

  public boolean removeAttackerClone(Troop fallenTroop) {
    return this.attackerClones.remove(fallenTroop);
  }

  public boolean removeDefenderClone(Troop fallenTroop) {
    return this.defenderClones.remove(fallenTroop);
  }

  public List<Troop> getAttackerClones() {
    return attackerClones;
  }

  public void setAttackerClones(List<Troop> attackerClones) {
    this.attackerClones = attackerClones;
  }

  public List<Troop> getDefenderClones() {
    return defenderClones;
  }

  public void setDefenderClones(List<Troop> defenderClones) {
    this.defenderClones = defenderClones;
  }

  public Kingdom getAttacker() {
    return attacker;
  }

  public Kingdom getDefender() {
    return defender;
  }

  public int getDistance() {
    return distance;
  }

  public int getTownhallLevel() {
    return townhallLevel;
  }

  public List<Troop> getFallenTroops() {
    return fallenTroops;
  }

}
