package com.itravel.api.payment.service;

import com.itravel.api.payment.model.dto.JwtResponse;
import com.itravel.api.payment.model.entitydto.AccountEntity;

public interface AuthPortalService {

  JwtResponse getCurrentJwt(String token);

  AccountEntity getAccountEntityByToken(String token);

}
