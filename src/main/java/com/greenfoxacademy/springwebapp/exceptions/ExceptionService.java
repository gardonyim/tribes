package com.greenfoxacademy.springwebapp.exceptions;

import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface ExceptionService {
  ErrorDTO createInputParamsValidErrorMessage(MethodArgumentNotValidException e);
}
