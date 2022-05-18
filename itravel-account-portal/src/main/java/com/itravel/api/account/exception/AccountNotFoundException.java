package com.itravel.api.account.exception;

import com.itravel.api.account.constraint.ErrorCode;
import com.itravel.api.account.constraint.ErrorMessage;

public class AccountNotFoundException extends ApplicationException {

  public AccountNotFoundException() {
    super(ErrorCode.ACCOUNT_NOT_FOUND, ErrorMessage.ACCOUNT_NOT_FOUND);
  }

}
