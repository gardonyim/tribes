package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

  @ExceptionHandler(RequestParameterMissingException.class)
  public ResponseEntity handleShortPassword(RequestParameterMissingException e) {
    return ResponseEntity.status(400).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(RequestNotAcceptableException.class)
  public ResponseEntity handleShortPassword(RequestNotAcceptableException e) {
    return ResponseEntity.status(406).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(RequestCauseConflictException.class)
  public ResponseEntity handleShortPassword(RequestCauseConflictException e) {
    return ResponseEntity.status(409).body(new ErrorDTO(e.getMessage()));
  }

  @PostMapping("/register")
  public ResponseEntity registerUser(@RequestBody RegistrationReqDTO reqDTO) {
    return ResponseEntity.status(201).body(playerService.savePlayer(reqDTO));
  }
}
