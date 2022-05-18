package com.itravel.api.account.service;

import com.itravel.api.account.exception.ApplicationException;
import com.itravel.api.account.model.dto.PagingEntityDto;
import com.itravel.api.account.model.entity.SellerInfoEntity;
import org.springframework.data.domain.Pageable;

public interface VerifySellerService {

  PagingEntityDto getAllSellerNotVerified(Pageable pageable);

  SellerInfoEntity getSellerNotVerifiedByAccountId(Long accountId) throws ApplicationException;

  void acceptSellerByAccountId(Long accountId) throws ApplicationException;

  void denySellerByAccountId(Long accountId) throws ApplicationException;
}
