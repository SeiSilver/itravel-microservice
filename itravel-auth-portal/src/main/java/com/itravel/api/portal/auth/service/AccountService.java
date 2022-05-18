package com.itravel.api.portal.auth.service;

import com.itravel.api.portal.auth.enums.AccountStatus;
import com.itravel.api.portal.auth.enums.RoleType;
import com.itravel.api.portal.auth.exception.AccountNotFoundException;
import com.itravel.api.portal.auth.exception.ApplicationException;
import com.itravel.api.portal.auth.model.dto.AccountInfo;
import com.itravel.api.portal.auth.model.dto.PagingEntityDto;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import com.itravel.api.portal.auth.payload.AccountUpdateRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AccountService {

  void updateBasicInfo(Long accountId, AccountUpdateRequest accountUpdateRequest);

  List<AccountInfo> getAll();

  PagingEntityDto getAllWithPaging(Pageable pageable);

  List<AccountInfo> getAllByAccountIds(List<Long> ids);

  AccountEntity getAccountById(Long accountId) throws AccountNotFoundException;

  void addRole(AccountEntity accountVerified, Long accountId, RoleType roleType) throws ApplicationException;

  void deleteRole(AccountEntity accountVerified, Long accountId, RoleType roleType) throws ApplicationException;

  void updateStatusAccount(AccountEntity accountVerified, Long accountId, AccountStatus accountStatus) throws ApplicationException;

  void registerSeller(AccountEntity accountEntity);
}
