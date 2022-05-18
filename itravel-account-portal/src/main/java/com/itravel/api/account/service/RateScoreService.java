package com.itravel.api.account.service;

import com.itravel.api.account.exception.AccountNotFoundException;
import com.itravel.api.account.exception.DataExistedException;
import com.itravel.api.account.exception.RateScoreNotFoundException;
import com.itravel.api.account.model.dto.PagingEntityDto;
import com.itravel.api.account.model.dto.RateScoreInfo;
import com.itravel.api.account.model.entitydto.AccountEntity;
import com.itravel.api.account.payload.RateInfoResponse;
import com.itravel.api.account.payload.RateScoreRequest;
import org.springframework.data.domain.Pageable;

public interface RateScoreService {

  PagingEntityDto getRateByServiceId(Pageable pageable, Integer serviceId) throws RateScoreNotFoundException, AccountNotFoundException;

  RateScoreInfo createRate(AccountEntity accountVerified, RateScoreRequest rateScoreRequest) throws DataExistedException;

  void deleteRateByServiceId(AccountEntity accountVerified, Integer serviceId) throws RateScoreNotFoundException;

  RateInfoResponse getRateInfoByServiceId(Integer serviceId) throws RateScoreNotFoundException;

  void updateServiceRate(Integer mainServiceId);

}
