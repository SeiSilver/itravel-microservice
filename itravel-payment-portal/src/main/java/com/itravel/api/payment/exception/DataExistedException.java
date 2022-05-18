package com.itravel.api.payment.exception;

public class DataExistedException extends ApplicationException {

  public DataExistedException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }

}
