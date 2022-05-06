package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TroopsController {

  TroopService troopService;

  public TroopsController(TroopService troopService) {
    this.troopService = troopService;
  }

  @GetMapping("kingdom/troops")
  public ResponseEntity<TroopsDTO> getTroopsByKingdomId(Authentication auth) {
    Player player = (Player) auth.getPrincipal();
    TroopsDTO troopsByKingdom = troopService.getTroopsOfKingdom(player.getKingdom());
    return ResponseEntity.ok(troopsByKingdom);
  }

  @PostMapping("kingdom/troops")
  public ResponseEntity<TroopDTO> postTroops(@RequestBody TroopPostDTO troopPostDTO, Authentication auth)
          throws ForbiddenActionException {
    Player player = (Player) auth.getPrincipal();
    TroopDTO troop = troopService.saveTroop(player.getKingdom(), troopPostDTO);
    return ResponseEntity.status(201).body(troop);
  }

}