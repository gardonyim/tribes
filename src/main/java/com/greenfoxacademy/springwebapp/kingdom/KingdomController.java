package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
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
  public ResponseEntity getKingdom(@PathVariable(required = false, value = "id") String kingdomId,
                                   UsernamePasswordAuthenticationToken user) {
    Player player = (Player)user.getPrincipal();
    int myKingdomId = player.getKingdom().getId();
    if (kingdomId == null || (kingdomId.matches("\\d+") && (Integer.valueOf(kingdomId) == myKingdomId))) {
      return ResponseEntity
          .status(200)
          .body(kingdomService.convertToKingdomResFullDTO(player.getKingdom()));
    } else if (kingdomId.matches("\\d+")) {
      return ResponseEntity
          .status(200)
          .body(kingdomService.convertToKingdomResWrappedDTO(kingdomService.findById(Integer.valueOf(kingdomId))));
    } else {
      throw new ForbiddenActionException();
    }

  }
}
