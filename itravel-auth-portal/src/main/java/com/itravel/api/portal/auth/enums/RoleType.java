package com.itravel.api.portal.auth.enums;

import com.itravel.api.portal.auth.exception.ApplicationException;
import java.util.Arrays;

public enum RoleType {

  ROLE_USER,
  ROLE_MODERATOR,
  ROLE_ADMIN,
  ROLE_SELLER;

  public static RoleType convert(String value) throws ApplicationException {
    return Arrays.stream(RoleType.values())
        .filter(o -> o.name().substring(5).equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new ApplicationException(30, "Invalid Role Name"));
  }

}
