package com.itravel.api.shop.controller.exceptionhandle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itravel.api.shop.controller.*;
import com.itravel.api.shop.error.ErrorDetail;
import com.itravel.api.shop.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeParseException;

@ControllerAdvice(assignableTypes = {ShopController.class, CartController.class, ServiceController.class, OrderController.class})
@Slf4j
@RestController
public class ShopPortalExceptionHandle {
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

    @ExceptionHandler({JsonProcessingException.class, DateTimeParseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDetail handleJsonExceptions(JsonProcessingException ex){
      log.error(FOUND_AN_ERROR, ex.getMessage(), ex);
      return ErrorDetail
          .builder()
          .errorCode(HttpStatus.BAD_REQUEST.value())
          .errorMessage(ex.getMessage())
          .build();
    }

}
