package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationResDTO;

import java.util.Optional;

public interface PlayerService {

  public RegistrationResDTO savePlayer(RegistrationReqDTO reqDTO);

  Optional<Player> findByName(String username);

}
