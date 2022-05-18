package com.itravel.api.payment.constraint;

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
  public static final String SELLER_INFO_NOT_FOUND = "Seller info not found!";
  public static final String SELLER_INFO_EXISTED = "Seller info existed!";
  public static final String FILE_UPLOAD_FAIL = "File upload fail!";
  public static final String FILE_TYPE_INVALID = "This file extension is invalid!";
  public static final String ACCOUNT_LOCKED = "Account is locked!";
  public static final String RATE_SCORE_NOT_FOUND = "Rate score not found!";
  public static final String RATE_SCORE_EXISTED = "This rate score for this service already existed!";
  public static final String DISCUSS_NOT_FOUND = "Discuss not found!";
  public static final String ORDER_NOT_FOUND = "Order not found";
  public static final String NOT_ORDER_OWNER = "Account is not order owner";
  public static final String NOT_SHOP_OWNER = "Account is not shop owner";

}
