package com.itravel.api.portal.auth.exception;

import com.itravel.api.portal.auth.constraint.ErrorCode;
import com.itravel.api.portal.auth.constraint.ErrorMessage;

public class InvalidJsonException extends ApplicationException {

  public InvalidJsonException() {
    super(ErrorCode.INVALID_JSON_PAYLOAD, ErrorMessage.INVALID_JSON_PAYLOAD);
  }

  public InvalidJsonException(Throwable cause) {
    super(ErrorCode.INVALID_JSON_PAYLOAD, ErrorMessage.INVALID_JSON_PAYLOAD, cause);
  }
}
