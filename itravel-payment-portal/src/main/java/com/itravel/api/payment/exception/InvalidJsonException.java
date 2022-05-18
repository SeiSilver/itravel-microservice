package com.itravel.api.payment.exception;

import com.itravel.api.payment.constraint.ErrorCode;
import com.itravel.api.payment.constraint.ErrorMessage;

public class InvalidJsonException extends ApplicationException {

  public InvalidJsonException() {
    super(ErrorCode.INVALID_JSON_PAYLOAD, ErrorMessage.INVALID_JSON_PAYLOAD);
  }

  public InvalidJsonException(Throwable cause) {
    super(ErrorCode.INVALID_JSON_PAYLOAD, ErrorMessage.INVALID_JSON_PAYLOAD, cause);
  }
}
