package com.greenfoxacademy.springwebapp.exceptions;

import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ExceptionServiceImpl implements ExceptionService {
  @Override
  public ErrorDTO createInputParamsValidErrorMessage(MethodArgumentNotValidException e) {
    StringBuilder errorDtoMessage = new StringBuilder();
    List<String> errorMessages = new ArrayList<>();
    e.getBindingResult().getAllErrors().forEach((objectError -> {
      String fieldName = ((FieldError)objectError).getField();
      String errorMessage = objectError.getDefaultMessage();
      errorMessages.add(fieldName + ": " + errorMessage);
    }));
    errorMessages.sort(Comparator.naturalOrder());
    errorMessages.stream().forEach(em -> errorDtoMessage.append(em).append("; "));
    return new ErrorDTO(errorDtoMessage.substring(0, errorDtoMessage.length() - 2));
  }
}
