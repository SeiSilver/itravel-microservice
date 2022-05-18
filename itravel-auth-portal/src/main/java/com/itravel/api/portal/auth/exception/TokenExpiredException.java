package com.itravel.api.portal.auth.exception;

import com.itravel.api.portal.auth.constraint.ErrorCode;
import com.itravel.api.portal.auth.constraint.ErrorMessage;

public class TokenExpiredException extends ApplicationException {

  public TokenExpiredException() {
    super(ErrorCode.TOKEN_EXPIRED, ErrorMessage.TOKEN_EXPIRED);
  }

  public TokenExpiredException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }
}
