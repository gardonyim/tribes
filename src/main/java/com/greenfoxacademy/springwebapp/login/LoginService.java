package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.login.dtos.LoginDTO;
import com.greenfoxacademy.springwebapp.login.dtos.StatusResponseDTO;
import com.greenfoxacademy.springwebapp.login.exceptions.InputMissingException;
import com.greenfoxacademy.springwebapp.login.exceptions.InputWrongException;
import com.greenfoxacademy.springwebapp.player.models.Player;

import java.util.Optional;

public interface LoginService {

  Optional<Player> findPlayerByName(String username);

  StatusResponseDTO authenticateWithLoginDTO(LoginDTO loginDTO)
          throws InputMissingException, InputWrongException;
}
