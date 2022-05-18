package com.itravel.api.portal.auth.service;

import com.itravel.api.portal.auth.exception.ApplicationException;
import com.itravel.api.portal.auth.model.dto.PagingEntityDto;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import org.springframework.data.domain.Pageable;

public interface AdminService {

  void lockAccount(AccountEntity accountVerified, Long accountId) throws ApplicationException;

  void unlockAccount(AccountEntity accountVerified, Long accountId) throws ApplicationException;

  PagingEntityDto getAllModeratorAccountWithPaging(Pageable pageable);

  PagingEntityDto getAllModeratorAccountWithPagingByQuery(Pageable pageable, String query);

  void addModeratorAccount(AccountEntity accountVerified, String email) throws ApplicationException;
}
