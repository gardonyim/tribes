package com.greenfoxacademy.springwebapp.player.models;

public class RegistrationReqDTO {

  private String username;
  private String password;
  private String kingdomname;
  private String email;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
