package com.itravel.api.account.service.impl;

import com.itravel.api.account.config.ApplicationProperties;
import com.itravel.api.account.constraint.ErrorCode;
import com.itravel.api.account.constraint.ErrorMessage;
import com.itravel.api.account.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.account.exception.AccountNotFoundException;
import com.itravel.api.account.exception.DataExistedException;
import com.itravel.api.account.exception.RateScoreNotFoundException;
import com.itravel.api.account.model.dto.AccountBasicInfo;
import com.itravel.api.account.model.dto.PagingEntityDto;
import com.itravel.api.account.model.dto.RateScoreInfo;
import com.itravel.api.account.model.entity.RateScoreEntity;
import com.itravel.api.account.model.entitydto.AccountEntity;
import com.itravel.api.account.model.jpa.RateScoreRepository;
import com.itravel.api.account.payload.GetAccountsByIdsPayload;
import com.itravel.api.account.payload.RateInfoResponse;
import com.itravel.api.account.payload.RateScoreRequest;
import com.itravel.api.account.payload.UpdateRateRequest;
import com.itravel.api.account.service.AccountService;
import com.itravel.api.account.service.MessagingService;
import com.itravel.api.account.service.RateScoreService;
import com.itravel.api.account.util.ModelMapperUtils;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class RateScoreServiceImpl implements RateScoreService {

  private final RateScoreRepository rateScoreRepository;
  private final ModelMapperUtils modelMapperUtils;
  private final AccountService accountService;
  private final MessagingService messagingService;

  @Override
  public PagingEntityDto getRateByServiceId(Pageable pageable, Integer serviceId) throws RateScoreNotFoundException, AccountNotFoundException {
    var pages = rateScoreRepository.findByMainServiceId(pageable, serviceId);
    if (pages.isEmpty()) {
      throw new RateScoreNotFoundException();
    }
    var accountIds = pages.getContent().stream()
        .map(RateScoreEntity::getAccountId)
        .collect(Collectors.toList());
    GetAccountsByIdsPayload accountsByIdsPayload = GetAccountsByIdsPayload.builder().listId(accountIds).build();
    List<AccountBasicInfo> listAccount = accountService.getAccountByAccountIds(accountsByIdsPayload);
    var result = modelMapperUtils.mapList(pages.getContent(), RateScoreInfo.class);
    for (int i = 0; i < result.size(); i++) {
      var temp = result.get(i);
      for (var account : listAccount) {
        if (Objects.equals(account.getId(), temp.getAccountId())) {
          temp.setAccount(account);
          result.set(i, temp);
          break;
        }
      }
    }
    return PagingEntityDto
        .builder()
        .page(pages.getNumber() + 1)
        .totalResults(pages.getTotalElements())
        .totalPages(pages.getTotalPages())
        .results(Collections.singletonList(result))
        .build();
  }

  @Override
  @Transactional
  public RateScoreInfo createRate(AccountEntity accountVerified, RateScoreRequest rateScoreRequest) throws DataExistedException {

    var temp = rateScoreRepository.findByAccountIdAndMainServiceId(accountVerified.getId(), rateScoreRequest.getMainServiceId());

    if (temp.isPresent()) {
      throw new DataExistedException(ErrorCode.RATE_SCORE_EXISTED, ErrorMessage.RATE_SCORE_EXISTED);
    }

    if (rateScoreRequest.getRatePoint() >  5) {
      throw new DataExistedException(ErrorCode.RATE_SCORE_INVALID, ErrorMessage.RATE_SCORE_EXISTED);
    }

    RateScoreEntity entity = RateScoreEntity.builder()
        .accountId(accountVerified.getId())
        .comment(rateScoreRequest.getComment())
        .mainServiceId(rateScoreRequest.getMainServiceId())
        .ratePoint(rateScoreRequest.getRatePoint())
        .build();

    var rateEntity = rateScoreRepository.save(entity);
    var dto = modelMapperUtils.map(rateEntity, RateScoreInfo.class);
    dto.setAccount(AccountBasicInfo.builder()
        .id(accountVerified.getId())
        .email(accountVerified.getEmail())
        .fullName(accountVerified.getFullName())
        .imageLink(accountVerified.getImageLink())
        .build());

    return dto;
  }

  @Override
  @Transactional
  public void deleteRateByServiceId(AccountEntity accountVerified, Integer serviceId) throws RateScoreNotFoundException {

    var rateScore = rateScoreRepository
        .findByAccountIdAndMainServiceId(accountVerified.getId(),
            serviceId).orElseThrow(RateScoreNotFoundException::new);
    rateScoreRepository.delete(rateScore);

  }

  @Override
  public RateInfoResponse getRateInfoByServiceId(Integer serviceId) throws RateScoreNotFoundException {

    var rateInfo = rateScoreRepository.findByMainServiceId(serviceId);
    if (rateInfo.isEmpty()) {
      throw new RateScoreNotFoundException();
    }

    Double averageScore = 0.0;
    for (var rate : rateInfo) {
      averageScore += rate.getRatePoint();
    }
    averageScore = averageScore / rateInfo.size();

    return RateInfoResponse.builder()
        .averageScore(averageScore)
        .numberOfRate(rateInfo.size())
        .build();
  }

  @Override
  public void updateServiceRate(Integer mainServiceId) {
    Long count = rateScoreRepository.countByMainServiceId(mainServiceId);
    Long total = rateScoreRepository.findRateScoreSum(mainServiceId);
    Float average = total.floatValue()/count;
    messagingService.sendUpdateServiceRate(
        UpdateRateRequest.builder()
            .rateAverage(average)
            .rateCount(count)
            .mainServiceId(mainServiceId)
            .build()
    );
  }

}
