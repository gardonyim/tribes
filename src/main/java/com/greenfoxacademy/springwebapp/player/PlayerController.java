package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@RestController
public class PlayerController {

  private PlayerService playerService;

  @Autowired
  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @PostMapping("/register")
  public ResponseEntity registerUser(@Valid @RequestBody RegistrationReqDTO reqDTO) {
    return ResponseEntity.status(201).body(playerService.savePlayer(reqDTO));
  }

  @GetMapping("/players")
  public ResponseEntity getPlayers(UsernamePasswordAuthenticationToken user,
                                   @RequestParam(required = false) Integer distance) {
    return ResponseEntity.ok(
        playerService.findNearbyPlayers((Player) user.getPrincipal(), distance));
  }

}
