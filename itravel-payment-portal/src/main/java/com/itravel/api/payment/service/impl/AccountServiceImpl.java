package com.itravel.api.payment.service.impl;

import com.itravel.api.payment.config.ApplicationProperties;
import com.itravel.api.payment.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.payment.exception.AccountNotFoundException;
import com.itravel.api.payment.model.dto.AccountBasicInfo;
import com.itravel.api.payment.payload.GetAccountsByIdsPayload;
import com.itravel.api.payment.service.AccountService;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final ApplicationProperties applicationProperties;
  private WebClient client;

  @Override
  public List<AccountBasicInfo> getAccountByAccountIds(GetAccountsByIdsPayload payload) throws AccountNotFoundException {
    client = WebClient.create(applicationProperties.getAuthPortal().getBaseUrl());
    Mono<List<AccountBasicInfo>> accountBasic = client.post()
        .uri(AuthPortalEndPoint.PUBLIC + AuthPortalEndPoint.PUBLIC_ACCOUNTS_INFO)
        .body(Mono.just(payload), GetAccountsByIdsPayload.class)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<AccountBasicInfo>>() {
        })
        .timeout(Duration.ofMillis(30000));
    List<AccountBasicInfo> listAccount = accountBasic.block();
    if (listAccount == null) {
      throw new AccountNotFoundException();
    }
    return listAccount;
  }

}
