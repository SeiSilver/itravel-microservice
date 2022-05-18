package com.itravel.api.payment.controller.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthPortalEndPoint {

  public static final String AUTH = "/auth";

  public static final String AUTH_TOKEN = "/token";

  public static final String AUTH_ACCOUNT = "/account";

  public static final String PAYPAL = "/paypal";

  public static final String PAYPAL_PAY = "/pay";

  public static final String PAYPAL_PAY_SUCCESS = PAYPAL_PAY + "/success";

  public static final String PAYPAL_PAY_CANCEL = PAYPAL_PAY + "/cancel";

  public static final String REFUND = "/refund";

  public static final String REFUND_CREATE = "/new";

  public static final String REFUND_DELETE_BY_ID = "/{refundRequestId}";

  public static final String REFUND_GET_BY_ID = "/{refundRequestId}";

  public static final String REFUND_GET_BY_ACCOUNT_ID = "/all/{accountId}";

  public static final String REFUND_GET_BY_ORDER_BILL_ID = "/all/search";

  public static final String REFUND_GET_ALL = "/all";

  public static final String REFUND_ACCEPT_BY_ID = "/accept/{refundRequestId}";

  public static final String REFUND_DENY_BY_ID = "/deny/{refundRequestId}";

  public static final String PUBLIC = "/public";

  public static final String PUBLIC_ACCOUNTS_INFO = "/accounts";

}
