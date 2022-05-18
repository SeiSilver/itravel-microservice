package com.itravel.api.account.service;

import com.itravel.api.account.exception.AccountNotFoundException;
import com.itravel.api.account.exception.DiscussNotFoundException;
import com.itravel.api.account.model.dto.DiscussInfo;
import com.itravel.api.account.model.dto.PagingEntityDto;
import com.itravel.api.account.model.entitydto.AccountEntity;
import com.itravel.api.account.payload.DiscussCreateRequest;
import com.itravel.api.account.payload.DiscussUpdateRequest;
import org.springframework.data.domain.Pageable;

public interface DiscussService {

  PagingEntityDto getDiscussByServiceId(Pageable pageable, Integer serviceId) throws DiscussNotFoundException, AccountNotFoundException;

  DiscussInfo createDiscuss(AccountEntity accountVerified, DiscussCreateRequest discussCreateRequest);

  void updateDiscussByDiscussId(AccountEntity accountVerified, DiscussUpdateRequest discussUpdateRequest) throws DiscussNotFoundException;

  void deleteDiscussByDiscussId(AccountEntity accountVerified, Integer discussId) throws DiscussNotFoundException;

}
