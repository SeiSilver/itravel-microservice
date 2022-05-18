package com.itravel.api.portal.auth.controller;

import com.itravel.api.portal.auth.annotation.CurrentUser;
import com.itravel.api.portal.auth.constraint.CommonKey;
import com.itravel.api.portal.auth.controller.endpoint.SystemPortalEndPoint;
import com.itravel.api.portal.auth.enums.AccountStatus;
import com.itravel.api.portal.auth.enums.RoleType;
import com.itravel.api.portal.auth.exception.AccountNotFoundException;
import com.itravel.api.portal.auth.exception.ApplicationException;
import com.itravel.api.portal.auth.model.dto.PagingEntityDto;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import com.itravel.api.portal.auth.security.principal.AccountPrincipal;
import com.itravel.api.portal.auth.service.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(SystemPortalEndPoint.SYSTEM)
@RequiredArgsConstructor
@Log4j2
public class SystemPortalController {

  private final AccountService accountService;

  @GetMapping(SystemPortalEndPoint.SYSTEM_ACCOUNT_ALL)
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @ApiOperation(value = "Get all account info")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public PagingEntityDto getAllAccount(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    if (page <= 1) {
      page = 0;
    } else {
      page--;
    }
    Pageable paging = PageRequest.of(page, size);
    return accountService.getAllWithPaging(paging);
  }

  @GetMapping(SystemPortalEndPoint.SYSTEM_ACCOUNT_BY_ACCOUNT_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @ApiOperation(value = "Get account info by account id")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public AccountEntity getAccountById(@PathVariable Long accountId) throws AccountNotFoundException {
    return accountService.getAccountById(accountId);
  }

  @PutMapping(SystemPortalEndPoint.SYSTEM_LOCK_ACCOUNT_BY_ACCOUNT_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @ApiOperation(value = "Lock account by Id")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void lockAccount(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @PathVariable Long accountId) throws ApplicationException {
    accountService.updateStatusAccount(userPrincipal.getAccountVerified(), accountId, AccountStatus.LOCKED);
  }

  @PutMapping(SystemPortalEndPoint.SYSTEM_UNLOCK_ACCOUNT_BY_ACCOUNT_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @ApiOperation(value = "Unlock lock account by Id")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void unlockAccount(@ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @PathVariable Long accountId) throws ApplicationException {
    accountService.updateStatusAccount(userPrincipal.getAccountVerified(), accountId, AccountStatus.ACTIVE);
  }

  @PostMapping(SystemPortalEndPoint.SYSTEM_ACCOUNT_ROLE)
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @ApiOperation(value = "Add user role")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void addRole(@ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @RequestParam Long accountId, @RequestParam String roleName)
      throws ApplicationException {
    accountService.addRole(userPrincipal.getAccountVerified(), accountId, RoleType.convert(roleName.toUpperCase()));
  }

  @DeleteMapping(SystemPortalEndPoint.SYSTEM_ACCOUNT_ROLE)
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @ApiOperation(value = "delete user role")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void deleteRole(@ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @RequestParam Long accountId, @RequestParam String roleName)
      throws ApplicationException {
    accountService.deleteRole(userPrincipal.getAccountVerified(), accountId, RoleType.convert(roleName.toUpperCase()));
  }

  @PostMapping(SystemPortalEndPoint.REGISTER)
  public ResponseEntity<String> registerSeller(
      @RequestBody Long accountId
  ) throws AccountNotFoundException {
    log.info("Receive register seller request for account {}", accountId);
    AccountEntity accountEntity = accountService.getAccountById(accountId);
    accountService.registerSeller(accountEntity);
    return ResponseEntity.ok("Success");
  }
}
