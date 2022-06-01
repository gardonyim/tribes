package com.greenfoxacademy.springwebapp.battle;

import com.greenfoxacademy.springwebapp.battle.models.BattleReqDTO;
import com.greenfoxacademy.springwebapp.battle.models.BattleResDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BattleController {

  private BattleService battleService;

  public BattleController(BattleService battleService) {
    this.battleService = battleService;
  }

  @PostMapping("/kingdom/{id}/battle")
  public ResponseEntity<BattleResDTO> battle(Authentication user, @PathVariable int id,
                                             @RequestBody BattleReqDTO reqDTO) {
    Kingdom kingdom = ((Player) user.getPrincipal()).getKingdom();
    return ResponseEntity.ok(battleService.battle(kingdom, id, reqDTO));
  }

}
