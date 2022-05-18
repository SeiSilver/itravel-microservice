package com.itravel.api.payment.constraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class S3Folder {

  public static final String SELLER_INFO = "SELLER_INFO";
  public static final String SHOP = "SHOP";
  public static final String MAIN_SERVICE = "MAIN_SERVICE";
  public static final String SUB_SERVICE = "SUB_SERVICE";
  public static final String CITY = "CITY";
  public static final String CATEGORY = "CATEGORY";
  public static final String REFUND = "REFUND";

}
