package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

  private PlayerService playerService;

  @Autowired
  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @PostMapping("/register")
  public ResponseEntity registerUser(@RequestBody RegistrationReqDTO reqDTO) {
    return ResponseEntity.status(201).body(playerService.savePlayer(reqDTO));
  }
}
