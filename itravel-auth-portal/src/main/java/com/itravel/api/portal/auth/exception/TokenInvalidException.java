package com.itravel.api.portal.auth.exception;

import com.itravel.api.portal.auth.constraint.ErrorCode;
import com.itravel.api.portal.auth.constraint.ErrorMessage;

public class TokenInvalidException extends ApplicationException {

  public TokenInvalidException() {
    super(ErrorCode.TOKEN_INVALID, ErrorMessage.TOKEN_INVALID);
  }


  public TokenInvalidException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }

  public TokenInvalidException(int errorCode, String errorMessage, Throwable rootCause) {
    super(errorCode, errorMessage, rootCause);
  }

}
