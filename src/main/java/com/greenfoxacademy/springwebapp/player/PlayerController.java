package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.exceptions.ParameterMissingException;
import com.greenfoxacademy.springwebapp.exceptions.ShortPasswordException;
import com.greenfoxacademy.springwebapp.exceptions.UsernameAlreadyExistsException;
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

  @ExceptionHandler(ParameterMissingException.class)
  public ResponseEntity handleShortPassword(ParameterMissingException e) {
    return ResponseEntity.status(400).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(ShortPasswordException.class)
  public ResponseEntity handleShortPassword(ShortPasswordException e) {
    return ResponseEntity.status(406).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  public ResponseEntity handleShortPassword(UsernameAlreadyExistsException e) {
    return ResponseEntity.status(409).body(new ErrorDTO(e.getMessage()));
  }

  @PostMapping("/register")
  public ResponseEntity registerUser(@RequestBody RegistrationReqDTO reqDTO) {
    return ResponseEntity.status(201).body(playerService.savePlayer(reqDTO));
  }
}
