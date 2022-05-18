package com.itravel.api.portal.auth.model.jpa;

import com.itravel.api.portal.auth.payload.AccountUpdateRequest;

public interface AccountCustomRepository {

  void updateBasicInfo(Long accountId, AccountUpdateRequest accountUpdateRequest);

}
