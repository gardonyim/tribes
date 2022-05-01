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

  @Autowired
  public GameObjectRuleHolder(GameObjectRuleService gameObjectRuleService) {
    this.gameObjectRuleService = gameObjectRuleService;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    gameObjectRules = gameObjectRuleService.findAll();
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
      return rule.getBuildingTimeMultiplierNInSec();
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