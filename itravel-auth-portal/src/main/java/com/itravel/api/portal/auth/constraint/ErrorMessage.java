package com.itravel.api.portal.auth.constraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {

  public static final String INVALID_JSON_PAYLOAD = "Payload parse exception";
  public static final String AUTHORIZED_FAIL = "Authentication Failed, please try again!";
  public static final String TOKEN_INVALID = "Invalid token!";
  public static final String TOKEN_EXPIRED = "Token expired!";
  public static final String ACCESS_DENIED = "Access denied!";
  public static final String ACCOUNT_NOT_FOUND = "Account not found!";
  public static final String ACCOUNT_LOCKED = "Account is locked!";
  public static final String INCORRECT_EMAIL_FORMAT = "Incorrect email format";
}
