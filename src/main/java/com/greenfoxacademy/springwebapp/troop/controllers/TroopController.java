package com.greenfoxacademy.springwebapp.troop.controllers;

import com.greenfoxacademy.springwebapp.troop.models.PutTroopDto;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/kingdom/troops")
public class TroopController {

  private final TroopService troopService;

  @Autowired
  public TroopController(TroopService troopService) {
    this.troopService = troopService;
  }

  @GetMapping
  public List<Troop> getTroopsOfKingdom() {
    return troopService.findAllTroops();
  }

  @PostMapping
  public void createNewTroop(Troop troop) {
    troopService.addNewTroop(troop);
  }

  @GetMapping("/{id}")
  public Optional<Troop> getTroopById(@PathVariable Integer id) {
    return troopService.getTroopById(id);
  }

  @PutMapping("/{id}")
  public void upgradeTroopToAcademyLevel(@PathVariable Integer id, @RequestBody PutTroopDto putTroopDto) {
    if (putTroopDto == null) {
      throw new IllegalArgumentException();
    }
    Integer buildingId = putTroopDto.getBuildingId();
    troopService.upgradeTroop(id, buildingId);
  }

}
