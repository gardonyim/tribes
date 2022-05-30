package com.greenfoxacademy.springwebapp.gamesettings;

import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameObjectRuleRepository extends CrudRepository<GameObjectRule, Integer> {

  List<GameObjectRule> findAll();
}
