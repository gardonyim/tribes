package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopsDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class TroopsController {

  TroopService troopService;

  public TroopsController(TroopService troopService) {
    this.troopService = troopService;
  }

  @GetMapping("kingdom/troops")
  public ResponseEntity<TroopsDTO> getTroopsByKingdomId() {

    Player player = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Integer kingdomId = player.getKingdom().getId();

    List<Troop> troopsByKingdom = troopService.getTroopsOfKingdom(kingdomId);

    List<TroopDTO> troopDTOs = troopService.mapTroopsToTroopDTO(troopsByKingdom);

    return ResponseEntity.ok(new TroopsDTO(troopDTOs));
  }

  @PostMapping("kingdom/troops")
  public ResponseEntity<TroopDTO> postTroops(@RequestBody TroopPostDTO troopPostDTO, Principal principal)
          throws ForbiddenActionException {
    Player player = (Player) principal;
    TroopDTO troop = troopService.saveTroop(player.getKingdom(), troopPostDTO);
    return ResponseEntity.status(201).body(troop);
  }

}