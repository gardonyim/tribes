package com.greenfoxacademy.springwebapp.gamesettings.model;

import com.greenfoxacademy.springwebapp.gamesettings.GameObjectRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GameObjectRuleHolder implements
    ApplicationListener<ContextRefreshedEvent> {

  private final GameObjectRuleService gameObjectRuleService;
  private List<GameObjectRule> gameObjectRules;
  public static final int ATTACK_MULTIPLIER = 10;
  public static final int DEFENCE_MULTIPLIER = 5;

  @Autowired
  public GameObjectRuleHolder(GameObjectRuleService gameObjectRuleService) {
    this.gameObjectRuleService = gameObjectRuleService;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    gameObjectRules = gameObjectRuleService.findAll();
  }

  public int calcCreationTime(String gameObjectType, int currentLevel, int reqLevel) {
    int totalTime = 0;
    if (reqLevel <= currentLevel) {
      return totalTime;
    }
    int nextLevel = ++currentLevel;
    while (nextLevel <= reqLevel) {
      totalTime += getBuildingTimeMultiplier(gameObjectType, nextLevel);
      nextLevel++;
    }
    return totalTime;
  }
  
  public int calcCreationCost(String gameObjectType, int currentLevel, int reqLevel) {
    int totalCost = 0;
    if (reqLevel <= currentLevel) {
      return totalCost;
    }
    int nextLevel = ++currentLevel;
    while (nextLevel <= reqLevel) {
      totalCost += getBuildingCostMultiplier(gameObjectType, nextLevel);
      nextLevel++;
    }
    return totalCost;
  }

  public int calcNewHP(String gameObjectType, int reqLevel) {
    return reqLevel * getHpMultiplier(gameObjectType, reqLevel);
  }

  public int calcNewAttack(String gameObjectType, int reqLevel) {
    return reqLevel * ATTACK_MULTIPLIER;
  }

  public int calcNewDefence(String gameObjectType, int reqLevel) {
    return reqLevel * DEFENCE_MULTIPLIER;
  }

  public int calcCreationCost(String gameObjectType, int currentLevel, int reqLevel) {
    int totalCost = 0;
    if (reqLevel <= currentLevel) {
      return totalCost;
    }
    int nextLevel = ++currentLevel;
    while (nextLevel <= reqLevel) {
      totalCost += nextLevel * getBuildingCostMultiplier(gameObjectType, nextLevel);
      nextLevel++;
    }
    return totalCost;
  }

  public int getBuildingTimeMultiplier(String gameObjectType, int level) {
    GameObjectRule rule = findByType(gameObjectType).orElseThrow(IllegalArgumentException::new);
    if (level == 1) {
      return rule.getBuildingTimeMultiplier1InSec();
    } else {
      return rule.getBuildingTimeMultiplierNInSec();
    }
  }

  public int getBuildingCostMultiplier(String gameObjectType, int level) {
    GameObjectRule rule = findByType(gameObjectType).orElseThrow(IllegalArgumentException::new);
    if (level == 1) {
      return rule.getBuildingCostMultiplier1();
    } else {
      return rule.getBuildingCostMultiplierN();
    }
  }

  public int getHpMultiplier(String gameObjectType, int level) {
    GameObjectRule rule = findByType(gameObjectType).orElseThrow(IllegalArgumentException::new);
    return rule.getHpMultiplier();
  }

  private Optional<GameObjectRule> findByType(String gameObjectType) {
    return gameObjectRules.stream()
        .filter(r -> r.getGameObjectType().equals(gameObjectType))
        .findFirst();
  }
}
