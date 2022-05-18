package com.itravel.api.portal.auth.exception;

import com.itravel.api.portal.auth.constraint.ErrorCode;
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
