package com.itravel.api.account.exception;

import com.itravel.api.account.constraint.ErrorCode;
import com.itravel.api.account.constraint.ErrorMessage;

public class SellerInfoExistedException extends ApplicationException {

  public SellerInfoExistedException() {
    super(ErrorCode.SELLER_INFO_EXISTED, ErrorMessage.SELLER_INFO_EXISTED);
  }
}
