package com.itravel.api.account.exception;

import com.itravel.api.account.constraint.ErrorCode;

public class S3UploadErrorException extends ApplicationException{

  public S3UploadErrorException(String errorMessage, Throwable rootCause) {
    super(ErrorCode.UNKNOWN_ERROR, errorMessage, rootCause);
  }

  public S3UploadErrorException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }
}
