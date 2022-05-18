package com.itravel.api.portal.auth.controller.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SystemPortalEndPoint {

  public static final String SYSTEM = "/system";

  public static final String SYSTEM_ACCOUNT_ALL = "/accounts";

  public static final String SYSTEM_ACCOUNT_BY_ACCOUNT_ID = "/account/{accountId}";

  public static final String SYSTEM_LOCK_ACCOUNT_BY_ACCOUNT_ID = "/lock/account/{accountId}";

  public static final String SYSTEM_UNLOCK_ACCOUNT_BY_ACCOUNT_ID = "/unlock/account/{accountId}";

  public static final String SYSTEM_ACCOUNT_ROLE = "/account/role";

  public static final String REGISTER = "/register";

}
