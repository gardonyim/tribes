package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KingdomController {

  private final KingdomService kingdomService;

  @Autowired
  public KingdomController(KingdomService kingdomService) {
    this.kingdomService = kingdomService;
  }

  @GetMapping({"/kingdom","/kingdom/{id}"})
  public ResponseEntity getKingdom(@PathVariable(required = false, value = "id") Integer kingdomId,
                                   UsernamePasswordAuthenticationToken user) {
    Kingdom kingdom = ((Player)user.getPrincipal()).getKingdom();
    if (kingdomId == null || kingdomId == kingdom.getId()) {
      return ResponseEntity.status(200).body(kingdomService.fetchKingdomData(kingdom));
    } else {
      return ResponseEntity.status(200).body(kingdomService.fetchKingdomData(kingdomId));
    }
  }

}

