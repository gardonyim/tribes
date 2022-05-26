package com.greenfoxacademy.springwebapp.player.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;

public class RegistrationReqDTO {

  @NotBlank(message = "Username is required.")
  private String username;
  @NotBlank(message = "Password is required.")
  @Size(min = 8, message = "Password must be at least 8 characters.")
  private String password;
  private String kingdomname;
<<<<<<< HEAD
  @Email(message = "Email have to be a valid e-mail address!")
=======
>>>>>>> 9720c4c (feat(Acticvation with e-mail): add EmailService)
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
<<<<<<< HEAD

=======
>>>>>>> 9720c4c (feat(Acticvation with e-mail): add EmailService)
}
