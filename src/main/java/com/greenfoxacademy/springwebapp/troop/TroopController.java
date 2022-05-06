package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kingdom/troops")
public class TroopController {

  private TroopService troopService;

  @Autowired
  public TroopController(TroopService troopService) {
    this.troopService = troopService;
  }

  @GetMapping("/{id}")
  public ResponseEntity handleGetTroopById(UsernamePasswordAuthenticationToken user,
                                           @PathVariable int id) {
    Kingdom kingdom = ((Player) user.getPrincipal()).getKingdom();
    return ResponseEntity.ok(troopService.fetchTroop(kingdom, id));
  }

}
