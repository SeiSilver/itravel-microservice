package com.itravel.api.portal.auth.security.service;

import com.itravel.api.portal.auth.model.entity.AccountEntity;
import com.itravel.api.portal.auth.model.jpa.AccountRepository;
import com.itravel.api.portal.auth.security.principal.AccountPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AccountRepository accountRepository;

  @Override
  @Transactional
  public AccountPrincipal loadUserByUsername(String email)
      throws UsernameNotFoundException {
    log.info("loadUserByName");
    AccountEntity account = accountRepository.findByEmail(email)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with email : " + email)
        );
    return AccountPrincipal.build(account);
  }

}
