package com.greenfoxacademy.springwebapp.gamesettings;

import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRule;

import java.util.List;

public interface GameObjectRuleService {

  List<GameObjectRule> findAll();
}
