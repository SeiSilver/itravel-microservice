package com.itravel.api.portal.auth.controller.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthPortalEndPoint {

  public static final String AUTH = "/auth";

  public static final String AUTH_TOKEN = "/token";

  public static final String AUTH_LOGIN = "/login";

  public static final String AUTH_VERIFY = "/verify";

  public static final String AUTH_ACCOUNT = "/account";

  public static final String ACCOUNT = "/account";

  public static final String ACCOUNT_ROLES = "/roles";

  public static final String ADMIN = "/admin";

  public static final String ADMIN_LOCK_ACCOUNT_BY_ACCOUNT_ID = "/lock/{accountId}";

  public static final String ADMIN_UNLOCK_ACCOUNT_BY_ACCOUNT_ID = "/unlock/{accountId}";

  public static final String ADMIN_GET_ACCOUNT_ALL = "/accounts";

  public static final String ADMIN_GET_ACCOUNT_ALL_BY_QUERY = "/search/accounts";

  public static final String MODERATOR = "/mod";

  public static final String MODERATOR_LOCK_ACCOUNT_BY_ACCOUNT_ID = "/lock/{accountId}";

  public static final String MODERATOR_UNLOCK_ACCOUNT_BY_ACCOUNT_ID = "/unlock/{accountId}";

  public static final String MODERATOR_GET_ACCOUNT_ALL = "/accounts";

  public static final String MODERATOR_GET_ACCOUNT_ALL_BY_QUERY = "/search/accounts";

  public static final String MODERATOR_ADD_NEW_MODERATOR = "/account/mod";

}
