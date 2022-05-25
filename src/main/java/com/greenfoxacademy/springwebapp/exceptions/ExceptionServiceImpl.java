package com.greenfoxacademy.springwebapp.exceptions;

import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
public class ExceptionServiceImpl implements ExceptionService {
  @Override
  public ErrorDTO createInputParamsValidErrorMessage(MethodArgumentNotValidException e) {
    StringBuilder sb = new StringBuilder();
    e.getBindingResult().getAllErrors().forEach((objectError -> {
      String fieldName = ((FieldError)objectError).getField();
      String errorMessage = objectError.getDefaultMessage();
      sb.append(fieldName).append(": ").append(errorMessage).append(", ");
    }));
    sb.substring(0, sb.length()-3);
    return new ErrorDTO(sb.toString());
  }
}
