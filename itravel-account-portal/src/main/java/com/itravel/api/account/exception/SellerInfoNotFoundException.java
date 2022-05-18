package com.itravel.api.account.exception;

import com.itravel.api.account.constraint.ErrorCode;
import com.itravel.api.account.constraint.ErrorMessage;

public class SellerInfoNotFoundException extends ApplicationException {

  public SellerInfoNotFoundException() {
    super(ErrorCode.SELLER_INFO_NOT_FOUND, ErrorMessage.SELLER_INFO_NOT_FOUND);
  }

}
