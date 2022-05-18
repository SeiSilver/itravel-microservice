package com.itravel.api.portal.auth.security.service;

import com.itravel.api.portal.auth.enums.AccountStatus;
import com.itravel.api.portal.auth.enums.RoleType;
import com.itravel.api.portal.auth.exception.OAuth2AuthenticationProcessingException;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import com.itravel.api.portal.auth.model.entity.RoleEntity;
import com.itravel.api.portal.auth.model.jpa.AccountRepository;
import com.itravel.api.portal.auth.model.jpa.RoleRepository;
import com.itravel.api.portal.auth.security.principal.AccountPrincipal;
import com.itravel.api.portal.auth.security.principal.GoogleOAuth2UserInfo;
import com.itravel.api.portal.auth.security.principal.OAuth2UserInfo;
import com.itravel.api.portal.auth.util.JwtUtils;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Log4j2
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final AccountRepository accountRepository;
  private final RoleRepository roleRepository;
  private final JwtUtils jwtUtils;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
      throws OAuth2AuthenticationException {
    log.info("load oauth2 user");
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    try {
      return processOAuth2User(oAuth2User);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
    OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
    if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
    }
    Optional<AccountEntity> accountDtoOpt = accountRepository.findByEmail(
        oAuth2UserInfo.getEmail());
    AccountEntity account;
    if (accountDtoOpt.isPresent()) {
      account = accountDtoOpt.get();
      account = updateExistingAccount(account, oAuth2UserInfo);
    } else {
      account = registerNewAccount(oAuth2UserInfo);
    }
    String token = jwtUtils.createToken(account.getEmail());
    log.info("token: {}", token);
    AccountPrincipal accountPrincipal = AccountPrincipal.build(account, oAuth2User.getAttributes());
    accountPrincipal.setAccessToken(token);
    return accountPrincipal;
  }

  private AccountEntity registerNewAccount(OAuth2UserInfo oAuth2UserInfo) {
    log.info("Try register new account");
    RoleEntity roleEntity = roleRepository.findByRoleName(RoleType.ROLE_USER)
        .orElse(RoleEntity.builder().id(1).roleName(RoleType.ROLE_USER).build());
    AccountEntity accountEntity = AccountEntity.builder()
        .fullName(oAuth2UserInfo.getName())
        .email(oAuth2UserInfo.getEmail())
        .imageLink(oAuth2UserInfo.getImageUrl())
        .roles(List.of(roleEntity))
        .status(AccountStatus.ACTIVE)
        .build();
    log.info("save oauth2 user");
    return accountRepository.save(accountEntity);
  }

  @Transactional
  public AccountEntity updateExistingAccount(AccountEntity account,
      OAuth2UserInfo oAuth2UserInfo) {
    log.info("Try update existing account");
    account.setFullName(oAuth2UserInfo.getName());
    account.setImageLink(oAuth2UserInfo.getImageUrl());
    log.info("update oauth2 user");
    return accountRepository.save(account);
  }
}
