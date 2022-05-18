package com.itravel.api.account.exception;

import com.itravel.api.account.constraint.ErrorCode;
import com.itravel.api.account.constraint.ErrorMessage;

public class TokenExpiredException extends ApplicationException {

  public TokenExpiredException() {
    super(ErrorCode.TOKEN_EXPIRED, ErrorMessage.TOKEN_EXPIRED);
  }

  public TokenExpiredException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }
}
