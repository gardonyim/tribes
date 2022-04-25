package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationResDTO;

public interface PlayerService {

  public RegistrationResDTO savePlayer(RegistrationReqDTO reqDTO);

}
