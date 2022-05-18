package com.itravel.api.portal.auth.service.impl;

import com.itravel.api.portal.auth.constraint.ErrorCode;
import com.itravel.api.portal.auth.enums.AccountStatus;
import com.itravel.api.portal.auth.enums.RoleType;
import com.itravel.api.portal.auth.exception.AccountNotFoundException;
import com.itravel.api.portal.auth.exception.ApplicationException;
import com.itravel.api.portal.auth.model.dto.AccountInfo;
import com.itravel.api.portal.auth.model.dto.PagingEntityDto;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import com.itravel.api.portal.auth.model.entity.RoleEntity;
import com.itravel.api.portal.auth.model.jpa.AccountRepository;
import com.itravel.api.portal.auth.model.jpa.RoleRepository;
import com.itravel.api.portal.auth.payload.AccountUpdateRequest;
import com.itravel.api.portal.auth.service.AccountService;
import com.itravel.api.portal.auth.util.ModelMapperUtils;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final ModelMapperUtils modelMapperUtils;
  private final RoleRepository roleRepository;

  @Override
  @Transactional
  public void updateBasicInfo(Long accountId, AccountUpdateRequest accountUpdateRequest) {
    accountRepository.updateBasicInfo(accountId, accountUpdateRequest);
  }

  @Override
  public List<AccountInfo> getAll() {
    return modelMapperUtils.mapList(accountRepository.findAll(), AccountInfo.class);
  }

  @Override
  public PagingEntityDto getAllWithPaging(Pageable pageable) {

    Page<AccountEntity> pages = accountRepository.findAll(pageable);
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
  public List<AccountInfo> getAllByAccountIds(List<Long> ids) {
    return modelMapperUtils.mapList(accountRepository.findByIdIn(ids), AccountInfo.class);
  }

  @Override
  public AccountEntity getAccountById(Long accountId) throws AccountNotFoundException {
    return accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
  }

  @Override
  @Transactional
  public void addRole(AccountEntity accountVerified, Long accountId, RoleType roleType) throws ApplicationException {
    var accountEntity = accountRepository.findById(accountId).orElseThrow(
        AccountNotFoundException::new
    );
    var accountRoles = accountEntity.getRoles();
    var tempRole = accountRoles.stream()
        .filter(o -> o.getRoleName().equals(roleType))
        .findAny();
    if (tempRole.isPresent()) {
      throw new ApplicationException(35, "This account already have " + roleType.name());
    }
    var roleEntity = roleRepository.findByRoleName(roleType).orElseThrow(
        () -> new ApplicationException(ErrorCode.UNKNOWN_ERROR, "")
    );
    accountRoles.add(roleEntity);
    accountEntity.setRoles(accountRoles);
    accountRepository.save(accountEntity);
  }


  @Override
  @Transactional
  public void deleteRole(AccountEntity accountVerified, Long accountId, RoleType roleType) throws ApplicationException {
    var accountEntity = accountRepository.findById(accountId).orElseThrow(
        AccountNotFoundException::new
    );
    var accountRoles = accountEntity.getRoles();
    var tempRole = accountRoles.stream()
        .filter(o -> o.getRoleName().equals(roleType))
        .findAny();
    if (tempRole.isEmpty()) {
      throw new ApplicationException(35, "This account dont have role " + roleType.name());
    }
    var roleEntity = roleRepository.findByRoleName(roleType).orElseThrow(
        () -> new ApplicationException(ErrorCode.UNKNOWN_ERROR, "")
    );
    accountRoles.remove(roleEntity);
    accountEntity.setRoles(accountRoles);
    accountRepository.save(accountEntity);
  }

  @Override
  @Transactional
  public void updateStatusAccount(AccountEntity accountVerified, Long accountId, AccountStatus accountStatus) throws ApplicationException {
    var accountEntity = accountRepository.findById(accountId).orElseThrow(
        AccountNotFoundException::new
    );
    if (Objects.equals(accountVerified.getId(), accountEntity.getId())) {
      throw new ApplicationException(33, "Cannot update Current login User status");
    }
    accountEntity.setStatus(accountStatus);
    accountRepository.save(accountEntity);
  }

  @Override
  public void registerSeller(AccountEntity accountEntity) {
    for(RoleEntity roleEntity : accountEntity.getRoles()){
      if(roleEntity.getRoleName().equals(RoleType.ROLE_SELLER))
        return;
    }
    List<RoleEntity> roles = accountEntity.getRoles();
    Optional<RoleEntity> roleEntityOpt = roleRepository.findByRoleName(RoleType.ROLE_SELLER);
    if(roleEntityOpt.isPresent()) {
      roles.add(roleEntityOpt.get());
      accountEntity.setRoles(roles);
      accountRepository.save(accountEntity);
    }
  }

}
