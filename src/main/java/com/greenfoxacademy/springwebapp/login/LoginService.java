package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.login.dtos.LoginDTO;
import com.greenfoxacademy.springwebapp.login.exceptions.InputMissingException;
import com.greenfoxacademy.springwebapp.login.exceptions.InputWrongException;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface LoginService {

  Optional<Player> authenticate(String username);

  ResponseEntity<Object> authenticateWithLoginDTO(LoginDTO loginDTO) throws InputMissingException, InputWrongException;
}
