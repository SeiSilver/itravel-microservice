package com.itravel.api.account.controller.exceptionhandler;

import com.itravel.api.account.error.ErrorDetail;
import com.itravel.api.account.exception.ApplicationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@Slf4j
@RestController
public class AccountPortalExceptionHandler {

  private static final String FOUND_AN_ERROR = "Found an error: {}";

  @ExceptionHandler({ApplicationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private ErrorDetail handleExceptions(ApplicationException ex) {
    log.error(FOUND_AN_ERROR, ex.getMessage(), ex);
    return ex.getErrorDetail();
  }

  @ExceptionHandler({MissingServletRequestParameterException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private ErrorDetail handleRequestParameterExceptions(MissingServletRequestParameterException ex) {
    log.error(FOUND_AN_ERROR, ex.getMessage(), ex);
    return ErrorDetail
        .builder()
        .errorCode(HttpStatus.BAD_REQUEST.value())
        .errorMessage(ex.getMessage())
        .build();
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private Map<String, Object> handleRequestParameterNotValidExceptions(MethodArgumentNotValidException ex) {
    log.error(FOUND_AN_ERROR, ex.getMessage(), ex);
    Map<String, Object> fieldError = new HashMap<>();
    ex.getBindingResult().getAllErrors()
        .forEach(error -> {
          String fieldName = ((FieldError) error).getField();
          String errorMessage = error.getDefaultMessage();
          fieldError.put(fieldName, errorMessage);
        });
    return new HashMap<>(generateErrorDetail(fieldError));
  }

  private Map<String, Object> generateErrorDetail(Object message) {
    Map<String, Object> data = new HashMap<>();
    data.put("errorCode", HttpStatus.BAD_REQUEST.value());
    data.put("errorMessage", message);
    return data;
  }

}
