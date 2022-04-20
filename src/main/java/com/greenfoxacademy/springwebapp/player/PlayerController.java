package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.exceptions.NoPasswordException;
import com.greenfoxacademy.springwebapp.exceptions.NoUsernameAndPasswordException;
import com.greenfoxacademy.springwebapp.exceptions.NoUsernameException;
import com.greenfoxacademy.springwebapp.exceptions.ShortPasswordException;
import com.greenfoxacademy.springwebapp.exceptions.UsernameAlreadyExistsException;
import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
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
    try {
      return ResponseEntity.status(201).body(playerService.savePlayer(reqDTO));
    } catch (NoPasswordException | NoUsernameException | NoUsernameAndPasswordException e) {
      return ResponseEntity.status(400).body(new ErrorDTO(e.getMessage()));
    } catch (ShortPasswordException e) {
      return ResponseEntity.status(406).body(new ErrorDTO(e.getMessage()));
    } catch (UsernameAlreadyExistsException e) {
      return ResponseEntity.status(409).body(new ErrorDTO(e.getMessage()));
    }

  }
}
