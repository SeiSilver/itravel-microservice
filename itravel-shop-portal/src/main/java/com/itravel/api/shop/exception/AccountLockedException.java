package com.itravel.api.shop.exception;

import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;

public class AccountLockedException extends ApplicationException {

  public AccountLockedException() {
    super(ErrorCode.ACCOUNT_LOCKED, ErrorMessage.ACCOUNT_LOCKED);
  }

}
