package com.itravel.api.account.exception;

import com.itravel.api.account.constraint.ErrorCode;
import com.itravel.api.account.constraint.ErrorMessage;

public class AccountLockedException extends ApplicationException {

  public AccountLockedException() {
    super(ErrorCode.ACCOUNT_LOCKED, ErrorMessage.ACCOUNT_LOCKED);
  }

}
