package com.greenfoxacademy.springwebapp.exceptions;

import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ExceptionServiceImpl implements ExceptionService {

  @Override
  public ErrorDTO createInputParamsValidErrorMessage(MethodArgumentNotValidException e) {
    List<String> errorMessages = new ArrayList<>();
    e.getBindingResult().getAllErrors().forEach((objectError -> {
      String errorMessage = objectError.getDefaultMessage();
      errorMessages.add(errorMessage);
    }));
    errorMessages.sort(Comparator.naturalOrder());
    return new ErrorDTO(createErrorMessage(errorMessages));
  }

  private String createErrorMessage(List<String> errorMessages) {
    List<String> blankFields = new ArrayList<>();
    for (String error : errorMessages) {
      if (error.contains("is required.")) {
        blankFields.add(error.split(" ")[0].toLowerCase());
      }
    }
    if (!blankFields.isEmpty()) {
      return createBlankFieldErrorMessage(blankFields);
    } else {
      return createNotBlankFieldErrorMessage(errorMessages);
    }
  }

  private String createBlankFieldErrorMessage(List<String> blankFields) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < blankFields.size(); i++) {
      if (i == 0) {
        sb.append(blankFields.get(0).substring(0,1).toUpperCase()).append(blankFields.get(0).substring(1));
      } else if (i == blankFields.size() - 1) {
        sb.append(" and ").append(blankFields.get(i));
      } else {
        sb.append(", ").append(blankFields.get(i));
      }
    }
    return sb + (blankFields.size() > 1 ? " are required." : " is required.");
  }

  private String createNotBlankFieldErrorMessage(List<String> errorMessages) {
    StringBuilder errorDtoMessage = new StringBuilder();
    errorMessages.stream().forEach(em -> errorDtoMessage.append(em).append("; "));
    return errorDtoMessage.substring(0, errorDtoMessage.length() - 2);
  }

}
