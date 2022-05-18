package com.itravel.api.account.constraint;


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
  public static final int SELLER_INFO_NOT_FOUND = 22;
  public static final int SELLER_INFO_EXISTED = 23;
  public static final int FILE_INVALID = 24;
  public static final int ACCOUNT_LOCKED = 30;
  public static final int RATE_SCORE_NOT_FOUND = 31;
  public static final int RATE_SCORE_EXISTED = 33;
  public static final int RATE_SCORE_INVALID = 34;

  public static final int DISCUSS_NOT_FOUND = 32;

  public static final int UNKNOWN_ERROR = 99;
}
