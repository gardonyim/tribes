package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.player.PlayerService;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private PlayerService playerService;

  @Autowired
  public UserController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @PostMapping("/register")
  public ResponseEntity registerUser(@RequestBody RegistrationReqDTO reqDTO) {
    return ResponseEntity.ok(playerService.savePlayer(reqDTO));
  }
}
