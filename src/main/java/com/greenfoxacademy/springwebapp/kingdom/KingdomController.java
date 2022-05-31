package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kingdom")
public class KingdomController {

  private final KingdomService kingdomService;

  @Autowired
  public KingdomController(KingdomService kingdomService) {
    this.kingdomService = kingdomService;
  }

  @GetMapping
  public ResponseEntity<KingdomResFullDTO> getKingdom(Authentication user) {
    return ResponseEntity
        .status(200)
        .body(kingdomService.fetchKingdomData(((Player)user.getPrincipal()).getKingdom()));
  }

  @GetMapping({"/{id}"})
  public ResponseEntity<KingdomResWrappedDTO> getKingdom(
      @PathVariable(required = false, value = "id") Integer kingdomId) {
    return ResponseEntity.status(200).body(kingdomService.fetchKingdomData(kingdomId));
  }

}
