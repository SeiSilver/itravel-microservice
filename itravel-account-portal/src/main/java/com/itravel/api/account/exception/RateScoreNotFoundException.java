package com.itravel.api.account.exception;

import com.itravel.api.account.constraint.ErrorCode;
import com.itravel.api.account.constraint.ErrorMessage;

public class RateScoreNotFoundException extends ApplicationException {

  public RateScoreNotFoundException() {
    super(ErrorCode.RATE_SCORE_NOT_FOUND, ErrorMessage.RATE_SCORE_NOT_FOUND);
  }
}
