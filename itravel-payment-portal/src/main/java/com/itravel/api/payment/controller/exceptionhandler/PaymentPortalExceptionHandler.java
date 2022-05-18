package com.itravel.api.payment.controller.exceptionhandler;

import com.itravel.api.payment.error.ErrorDetail;
import com.itravel.api.payment.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@Slf4j
@RestController
public class PaymentPortalExceptionHandler {

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

}
