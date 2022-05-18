package com.itravel.api.account.controller.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthPortalEndPoint {

  public static final String AUTH = "/auth";

  public static final String AUTH_TOKEN = "/token";

  public static final String AUTH_ACCOUNT = "/account";

  public static final String PUBLIC = "/public";

  public static final String PUBLIC_ACCOUNTS_INFO = "/accounts";
  
}
