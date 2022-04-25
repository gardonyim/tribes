package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.login.dtos.LoginDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {

  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/login")
  public ResponseEntity<Object> authentication(@RequestBody LoginDTO loginDTO) {
    return loginService.authenticateWithLoginDTO(loginDTO);
  }
}