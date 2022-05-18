package com.itravel.api.account.exception;

import com.itravel.api.account.constraint.ErrorCode;
import com.itravel.api.account.constraint.ErrorMessage;

public class DiscussNotFoundException extends ApplicationException {

  public DiscussNotFoundException() {
    super(ErrorCode.DISCUSS_NOT_FOUND, ErrorMessage.DISCUSS_NOT_FOUND);
  }

}
