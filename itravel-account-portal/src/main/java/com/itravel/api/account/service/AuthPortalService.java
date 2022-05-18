package com.itravel.api.account.service;

import com.itravel.api.account.model.dto.JwtResponse;
import com.itravel.api.account.model.entitydto.AccountEntity;

public interface AuthPortalService {

  JwtResponse getCurrentJwt(String token);

  AccountEntity getAccountEntityByToken(String token);

}
