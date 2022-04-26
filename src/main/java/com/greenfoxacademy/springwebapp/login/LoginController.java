package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.login.dtos.LoginDTO;
import com.greenfoxacademy.springwebapp.login.exceptions.InputMissingException;
import com.greenfoxacademy.springwebapp.login.exceptions.InputWrongException;
import com.greenfoxacademy.springwebapp.player.models.exceptions.models.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {

  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/login")
  public ResponseEntity<Object> authentication(@RequestBody LoginDTO loginDTO) throws InputMissingException, InputWrongException {
    return loginService.authenticateWithLoginDTO(loginDTO);
  }

  @ExceptionHandler(InputMissingException.class)
  public ResponseEntity<Object> handleMissingInput(InputMissingException e) {
    return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(InputWrongException.class)
  public ResponseEntity<Object> handleWrongInput(InputWrongException e) {
    return ResponseEntity.status(401).body(new ErrorDTO(e.getMessage()));
  }
}