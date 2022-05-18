package com.itravel.api.shop.service.impl;

import com.itravel.api.shop.config.ApplicationProperties;
import com.itravel.api.shop.constraint.CommonKey;
import com.itravel.api.shop.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.shop.model.dto.JwtResponse;
import com.itravel.api.shop.model.entity.AccountEntity;
import com.itravel.api.shop.service.AuthPortalService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthPortalServiceImpl implements AuthPortalService {

  private final ApplicationProperties applicationProperties;

  private WebClient client;

  @Override
  public JwtResponse getCurrentJwt(String token) {

    client = WebClient.create(applicationProperties.getAuthPortal().getBaseUrl());

    Mono<JwtResponse> response = client.get()
        .uri(AuthPortalEndPoint.AUTH + AuthPortalEndPoint.AUTH_TOKEN)
        .header(CommonKey.AUTHORIZATION, CommonKey.BEARER + " " + token)
        .retrieve()
        .bodyToMono(JwtResponse.class).timeout(Duration.ofMillis(10000));

    return response.block();
  }

  @Override
  public AccountEntity getAccountEntityByToken(String token) {
    client = WebClient.create(applicationProperties.getAuthPortal().getBaseUrl());

    Mono<AccountEntity> response = client.get()
        .uri(AuthPortalEndPoint.AUTH + AuthPortalEndPoint.AUTH_ACCOUNT)
        .header(CommonKey.AUTHORIZATION, CommonKey.BEARER + " " + token)
        .retrieve()
        .bodyToMono(AccountEntity.class).timeout(Duration.ofMillis(10000));
    return response.block();
  }
}
