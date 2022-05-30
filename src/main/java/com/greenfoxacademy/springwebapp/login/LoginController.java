package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.exceptions.ErrorDTO;
import com.greenfoxacademy.springwebapp.login.dtos.LoginDTO;
import com.greenfoxacademy.springwebapp.login.dtos.StatusResponseDTO;
import com.greenfoxacademy.springwebapp.login.exceptions.InputMissingException;
import com.greenfoxacademy.springwebapp.login.exceptions.InputWrongException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {

  private final LoginService loginService;

  @Autowired
  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/login")
  public ResponseEntity<StatusResponseDTO> authentication(@RequestBody LoginDTO loginDTO)
          throws InputMissingException, InputWrongException {
    StatusResponseDTO response = loginService.authenticateWithLoginDTO(loginDTO);
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler(InputMissingException.class)
  public ResponseEntity<ErrorDTO> handleMissingInput(InputMissingException e) {
    return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(InputWrongException.class)
  public ResponseEntity<ErrorDTO> handleWrongInput(InputWrongException e) {
    return ResponseEntity.status(401).body(new ErrorDTO(e.getMessage()));
  }
}