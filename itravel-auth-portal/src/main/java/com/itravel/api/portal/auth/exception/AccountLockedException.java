package com.itravel.api.portal.auth.exception;

import com.itravel.api.portal.auth.constraint.ErrorCode;
import com.itravel.api.portal.auth.constraint.ErrorMessage;

public class AccountLockedException extends ApplicationException {

  public AccountLockedException() {
    super(ErrorCode.ACCOUNT_LOCKED, ErrorMessage.ACCOUNT_LOCKED);
  }

}
