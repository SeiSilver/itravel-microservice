package com.itravel.api.account.exception;

public class DataExistedException extends ApplicationException {

  public DataExistedException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }

}
