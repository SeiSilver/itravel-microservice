package com.itravel.api.portal.auth.constraint;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCode {

  public static final int MISSING_REQUIRED_FIELD = 5;
  public static final int AUTHORIZED_FAIL = 6;
  public static final int ACCESS_DENIED = 7;

  public static final int TOKEN_INVALID = 15;
  public static final int TOKEN_EXPIRED = 16;
  public static final int INVALID_JSON_PAYLOAD = 20;
  public static final int ACCOUNT_NOT_FOUND = 21;
  public static final int ACCOUNT_LOCKED = 22;

  public static final int INCORRECT_EMAIL_FORMAT = 80;
  public static final int UNKNOWN_ERROR = 99;

}
