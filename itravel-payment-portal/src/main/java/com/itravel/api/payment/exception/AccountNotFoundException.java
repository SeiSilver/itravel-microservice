package com.itravel.api.payment.exception;

import com.itravel.api.payment.constraint.ErrorCode;
import com.itravel.api.payment.constraint.ErrorMessage;

public class AccountNotFoundException extends ApplicationException {

  public AccountNotFoundException() {
    super(ErrorCode.ACCOUNT_NOT_FOUND, ErrorMessage.ACCOUNT_NOT_FOUND);
  }

}
