package com.itravel.api.payment.exception;

import com.itravel.api.payment.constraint.ErrorCode;
import com.itravel.api.payment.constraint.ErrorMessage;

public class TokenExpiredException extends ApplicationException {

  public TokenExpiredException() {
    super(ErrorCode.TOKEN_EXPIRED, ErrorMessage.TOKEN_EXPIRED);
  }

  public TokenExpiredException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }
}
