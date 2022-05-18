package com.itravel.api.portal.auth.service;

import com.itravel.api.portal.auth.exception.ApplicationException;
import com.itravel.api.portal.auth.model.dto.PagingEntityDto;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import org.springframework.data.domain.Pageable;

public interface ModeratorService {

  void lockAccount(AccountEntity accountVerified, Long accountId) throws ApplicationException;

  void unlockAccount(AccountEntity accountVerified, Long accountId) throws ApplicationException;

  PagingEntityDto getAllUserSellerAccountWithPaging(Pageable pageable);

  PagingEntityDto getAllUserSellerAccountWithPagingByQuery(Pageable pageable, String query);

}
