package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;

public interface PlayerService {

  public Player savePlayer(RegistrationReqDTO reqDTO);

}
