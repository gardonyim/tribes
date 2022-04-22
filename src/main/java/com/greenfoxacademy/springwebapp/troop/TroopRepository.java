package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TroopRepository extends CrudRepository<Troop, Integer> {

  List<Troop> findAll();
}
