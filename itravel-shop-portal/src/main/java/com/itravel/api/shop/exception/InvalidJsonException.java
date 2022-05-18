package com.itravel.api.shop.exception;


import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;

public class InvalidJsonException extends ApplicationException {

  public InvalidJsonException() {
    super(ErrorCode.INVALID_JSON_PAYLOAD, ErrorMessage.INVALID_JSON_PAYLOAD);
  }

  public InvalidJsonException(Throwable cause) {
    super(ErrorCode.INVALID_JSON_PAYLOAD, ErrorMessage.INVALID_JSON_PAYLOAD, cause);
  }
}
