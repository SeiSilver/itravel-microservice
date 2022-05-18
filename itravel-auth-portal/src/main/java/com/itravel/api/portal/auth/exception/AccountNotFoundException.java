package com.itravel.api.portal.auth.exception;

import com.itravel.api.portal.auth.constraint.ErrorCode;
import com.itravel.api.portal.auth.constraint.ErrorMessage;

public class AccountNotFoundException extends ApplicationException {

  public AccountNotFoundException() {
    super(ErrorCode.ACCOUNT_NOT_FOUND, ErrorMessage.ACCOUNT_NOT_FOUND);
  }

}
