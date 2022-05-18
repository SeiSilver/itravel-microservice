package com.itravel.api.portal.auth.service.impl;

import com.itravel.api.portal.auth.enums.AccountStatus;
import com.itravel.api.portal.auth.enums.RoleType;
import com.itravel.api.portal.auth.exception.ApplicationException;
import com.itravel.api.portal.auth.model.dto.PagingEntityDto;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import com.itravel.api.portal.auth.model.jpa.AccountRepository;
import com.itravel.api.portal.auth.service.AccountService;
import com.itravel.api.portal.auth.service.ModeratorService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@AllArgsConstructor
public class ModeratorServiceImpl implements ModeratorService {

  private static final List<RoleType> ROLES = List.of(
      RoleType.ROLE_USER,
      RoleType.ROLE_SELLER
  );
  private final AccountRepository accountRepository;
  private final AccountService accountService;

  @Override
  @Transactional
  public void lockAccount(AccountEntity accountVerified, Long accountId) throws ApplicationException {
    var accountEntity = accountService.getAccountById(accountId);
    var adminUser = accountEntity.getRoles().stream()
        .filter(o -> o.getRoleName().equals(RoleType.ROLE_ADMIN))
        .findAny();
    if (adminUser.isPresent()) {
      throw new ApplicationException(32, "Cannot lock Admin User");
    }
    var moderatorUser = accountEntity.getRoles().stream()
        .filter(o -> o.getRoleName().equals(RoleType.ROLE_MODERATOR))
        .findAny();
    if (moderatorUser.isPresent()) {
      throw new ApplicationException(34, "Cannot lock other moderator User");
    }
    if (Objects.equals(accountVerified.getId(), accountEntity.getId())) {
      throw new ApplicationException(33, "Cannot lock Current login User");
    }
    accountEntity.setStatus(AccountStatus.LOCKED);
    accountRepository.save(accountEntity);
  }

  @Override
  @Transactional
  public void unlockAccount(AccountEntity accountVerified, Long accountId) throws ApplicationException {
    var accountEntity = accountService.getAccountById(accountId);
    if (Objects.equals(accountVerified.getId(), accountEntity.getId())) {
      throw new ApplicationException(33, "Cannot unlock Current login User");
    }
    var moderatorUser = accountEntity.getRoles().stream()
        .filter(o -> o.getRoleName().equals(RoleType.ROLE_MODERATOR))
        .findAny();
    if (moderatorUser.isPresent()) {
      throw new ApplicationException(34, "Cannot unlock other moderator User");
    }
    accountEntity.setStatus(AccountStatus.ACTIVE);
    accountRepository.save(accountEntity);
  }

  @Override
  public PagingEntityDto getAllUserSellerAccountWithPaging(Pageable pageable) {
    Page<AccountEntity> pages = accountRepository.findAllByRolesInOrderByIdAsc(pageable, ROLES);
    var content = pages.getContent();
    return PagingEntityDto
        .builder()
        .page(pages.getNumber() + 1)
        .totalResults(pages.getTotalElements())
        .totalPages(pages.getTotalPages())
        .results(Collections.singletonList(content))
        .build();
  }

  @Override
  public PagingEntityDto getAllUserSellerAccountWithPagingByQuery(Pageable pageable, String query) {
    Page<AccountEntity> pages = accountRepository.findAllByRolesInAndQuery(ROLES, query, pageable);
    var content = pages.getContent();
    return PagingEntityDto
        .builder()
        .page(pages.getNumber() + 1)
        .totalResults(pages.getTotalElements())
        .totalPages(pages.getTotalPages())
        .results(Collections.singletonList(content))
        .build();
  }

}
