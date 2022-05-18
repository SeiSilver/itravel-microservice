package com.itravel.api.shop.exception;

import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;

public class TokenExpiredException extends ApplicationException {

  public TokenExpiredException() {
    super(ErrorCode.TOKEN_EXPIRED, ErrorMessage.TOKEN_EXPIRED);
  }

  public TokenExpiredException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }
}
