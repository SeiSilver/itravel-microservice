package com.itravel.api.payment.controller.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountPortalEndPoint {

  public static final String ACCOUNT = "/account";

  public static final String ACCOUNT_SELLER_INFO = "/seller";

  public static final String ACCOUNT_NOTIFICATION = "/notification";

  public static final String RATE = "/rate";

  public static final String RATE_BY_SERVICE_ID = "/{serviceId}";

  public static final String DISCUSS = "/discuss";

  public static final String DISCUSS_BY_SERVICE_ID = "/{serviceId}";

  public static final String DISCUSS_DELETE_BY_ID = "/{discussId}";

  public static final String VERIFY = "/verify";

  public static final String ORDER = "/order";

  public static final String ORDER_ID = "/{orderId}";

  public static final String ORDER_ACCOUNT_ACCOUNT_ID = "/order/account/{accountId}";

  public static final String MOD = "/mod";

  public static final String FREQUENCY ="/frequency";

  public static final String STATUS = "/status";

  public static final String SHOP_SHOP_ID = "/shop/{shopId}";

  public static final String SHOP_SHOP_ID_ORDER_ID = "/shop/{shopId}/{orderId}";
}
