package com.itravel.api.payment.enums;

public enum AccountStatus {

  ACTIVE,
  DELETED,
  LOCKED,
  BLOCKED;

  public static AccountStatus convertFromString(String type) {
    var accountStatus = AccountStatus.values();
    for (var i : accountStatus) {
      if (i.name().equals(type)) {
        return i;
      }
    }
    return null;
  }
}
