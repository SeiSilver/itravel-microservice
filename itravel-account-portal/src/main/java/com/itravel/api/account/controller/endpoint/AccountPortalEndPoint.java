package com.itravel.api.account.controller.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountPortalEndPoint {

  public static final String ACCOUNT = "/account";

  public static final String ACCOUNT_SELLER_INFO = "/seller";

  public static final String ACCOUNT_NOTIFICATION = "/notification";

  public static final String RATE = "/rate";

  public static final String RATE_BY_SERVICE_ID = "/{serviceId}";

  public static final String RATE_INFO_BY_SERVICE_ID = "/info/{serviceId}";

  public static final String DISCUSS = "/discuss";

  public static final String DISCUSS_BY_SERVICE_ID = "/{serviceId}";

  public static final String DISCUSS_DELETE_BY_ID = "/{discussId}";

  public static final String VERIFY = "/verify";

  public static final String VERIFY_GET_ALL_SELLER_INFO = "/accounts";

  public static final String VERIFY_GET_SELLER_INFO = "/account/{accountId}";

  public static final String VERIFY_ACCEPT_SELLER_INFO = "/accept/{accountId}";

  public static final String VERIFY_DENY_SELLER_INFO = "/deny/{accountId}";

}
