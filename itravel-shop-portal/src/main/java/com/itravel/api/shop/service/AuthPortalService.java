package com.itravel.api.shop.service;


import com.itravel.api.shop.model.dto.JwtResponse;
import com.itravel.api.shop.model.entity.AccountEntity;

public interface AuthPortalService {

  JwtResponse getCurrentJwt(String token);

  AccountEntity getAccountEntityByToken(String token);


}
