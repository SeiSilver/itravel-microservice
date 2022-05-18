package com.itravel.api.shop.security.service;

import com.itravel.api.shop.model.entity.AccountEntity;
import com.itravel.api.shop.security.principal.AccountPrincipal;
import com.itravel.api.shop.service.AuthPortalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AuthPortalService authPortalService;

  @Override
  public AccountPrincipal loadUserByUsername(String token)
      throws UsernameNotFoundException {
    log.info("loadUser by using token");
    AccountEntity account = authPortalService.getAccountEntityByToken(token);
    return AccountPrincipal.build(account);
  }

}
