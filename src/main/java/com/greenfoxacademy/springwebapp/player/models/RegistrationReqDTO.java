package com.greenfoxacademy.springwebapp.player.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegistrationReqDTO {

  @NotBlank(message = "Username is required.")
  private String username;
  @NotBlank(message = "Password is required.")
  @Size(min = 8, message = "Password must be at least 8 characters.")
  private String password;
  private String kingdomname;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getKingdomname() {
    return kingdomname;
  }

  public void setKingdomname(String kingdomname) {
    this.kingdomname = kingdomname;
  }
}
