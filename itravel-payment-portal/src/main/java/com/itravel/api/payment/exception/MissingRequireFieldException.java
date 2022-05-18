package com.itravel.api.payment.exception;

import com.itravel.api.payment.constraint.ErrorCode;
import lombok.Getter;

@Getter
public class MissingRequireFieldException extends ApplicationException {

  public MissingRequireFieldException(String errorMessage) {
    this(ErrorCode.MISSING_REQUIRED_FIELD, errorMessage);
  }

  public MissingRequireFieldException(Integer errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }
}
