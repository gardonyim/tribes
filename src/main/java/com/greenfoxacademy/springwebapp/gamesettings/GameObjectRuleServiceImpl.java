package com.greenfoxacademy.springwebapp.gamesettings;

import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameObjectRuleServiceImpl implements GameObjectRuleService {

  private final GameObjectRuleRepository gameObjectRuleRepository;

  @Autowired
  public GameObjectRuleServiceImpl(GameObjectRuleRepository gameObjectRuleRepository) {
    this.gameObjectRuleRepository = gameObjectRuleRepository;
  }

  @Override
  public List<GameObjectRule> findAll() {
    return gameObjectRuleRepository.findAll();
  }
}
