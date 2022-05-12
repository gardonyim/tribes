package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomPutDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KingdomController {

  @Autowired
  KingdomService kingdomService;

  @PutMapping("/kingdom")
  public ResponseEntity<KingdomResFullDTO> modifyKingdomName(
          @RequestBody KingdomPutDTO kingdomPutDTO, Authentication auth) {
    kingdomService.checkKingdomPutDto(kingdomPutDTO);
    Player player = (Player) auth.getPrincipal();
    return ResponseEntity.ok(kingdomService.renameKingdom(player.getKingdom(), kingdomPutDTO.getName()));
  }
}