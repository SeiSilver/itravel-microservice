package com.itravel.api.shop.exception;


import com.itravel.api.shop.error.ErrorCode;

public class S3UploadErrorException extends ApplicationException{

  public S3UploadErrorException(String errorMessage, Throwable rootCause) {
    super(ErrorCode.UNKNOWN_ERROR, errorMessage, rootCause);
  }

  public S3UploadErrorException(int errorCode, String errorMessage) {
    super(errorCode, errorMessage);
  }
}
