package com.itravel.api.portal.auth.service.impl;

import com.itravel.api.portal.auth.enums.AccountStatus;
import com.itravel.api.portal.auth.enums.RoleType;
import com.itravel.api.portal.auth.exception.AccountNotFoundException;
import com.itravel.api.portal.auth.exception.ApplicationException;
import com.itravel.api.portal.auth.model.dto.PagingEntityDto;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import com.itravel.api.portal.auth.model.jpa.AccountRepository;
import com.itravel.api.portal.auth.service.AccountService;
import com.itravel.api.portal.auth.service.AdminService;
import com.itravel.api.portal.auth.util.CommonUtils;
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
public class AdminServiceImpl implements AdminService {

  private final AccountRepository accountRepository;
  private final AccountService accountService;
  private static final List<RoleType> ROLE_MODERATOR = List.of(
      RoleType.ROLE_MODERATOR
  );

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
    accountEntity.setStatus(AccountStatus.ACTIVE);
    accountRepository.save(accountEntity);
  }

  @Override
  public PagingEntityDto getAllModeratorAccountWithPaging(Pageable pageable) {
    Page<AccountEntity> pages = accountRepository.findAllByRolesInOrderByIdAsc(pageable, ROLE_MODERATOR);
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
  public PagingEntityDto getAllModeratorAccountWithPagingByQuery(Pageable pageable, String query) {
    Page<AccountEntity> pages = accountRepository.findAllByRolesInAndQuery(ROLE_MODERATOR, query, pageable);
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
  @Transactional
  public void addModeratorAccount(AccountEntity accountVerified, String email) throws ApplicationException {
    CommonUtils.checkFormatEmail(email);
    var accountEntity = accountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    accountService.addRole(accountVerified, accountEntity.getId(), RoleType.ROLE_MODERATOR);
  }

}
