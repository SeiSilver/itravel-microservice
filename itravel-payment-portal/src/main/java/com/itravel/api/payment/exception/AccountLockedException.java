package com.itravel.api.payment.exception;

import com.itravel.api.payment.constraint.ErrorCode;
import com.itravel.api.payment.constraint.ErrorMessage;

public class AccountLockedException extends ApplicationException {

  public AccountLockedException() {
    super(ErrorCode.ACCOUNT_LOCKED, ErrorMessage.ACCOUNT_LOCKED);
  }

}
