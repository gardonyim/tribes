package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.player.models.Player;

public interface LoginService {

  Player authenticate(String username, String password);
}
