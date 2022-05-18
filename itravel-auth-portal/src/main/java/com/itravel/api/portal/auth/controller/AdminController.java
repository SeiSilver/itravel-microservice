package com.itravel.api.portal.auth.controller;

import com.itravel.api.portal.auth.annotation.CurrentUser;
import com.itravel.api.portal.auth.constraint.CommonKey;
import com.itravel.api.portal.auth.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.portal.auth.exception.ApplicationException;
import com.itravel.api.portal.auth.model.dto.PagingEntityDto;
import com.itravel.api.portal.auth.security.principal.AccountPrincipal;
import com.itravel.api.portal.auth.service.AdminService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(AuthPortalEndPoint.ADMIN)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private final AdminService adminService;

  @PutMapping(AuthPortalEndPoint.ADMIN_LOCK_ACCOUNT_BY_ACCOUNT_ID)
  @ApiOperation(value = "Lock user, seller ,moderator account by Id")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void lockAccount(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @PathVariable Long accountId) throws ApplicationException {
    adminService.lockAccount(userPrincipal.getAccountVerified(), accountId);
  }

  @PutMapping(AuthPortalEndPoint.ADMIN_UNLOCK_ACCOUNT_BY_ACCOUNT_ID)
  @ApiOperation(value = "Unlock user, seller ,moderator account by Id")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void unlockAccount(@ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @PathVariable Long accountId) throws ApplicationException {
    adminService.unlockAccount(userPrincipal.getAccountVerified(), accountId);
  }

  @GetMapping(AuthPortalEndPoint.ADMIN_GET_ACCOUNT_ALL)
  @ApiOperation(value = "Get all moderator account")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public PagingEntityDto getAllModeratorAccount(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    if (page <= 1) {
      page = 0;
    } else {
      page--;
    }
    Pageable paging = PageRequest.of(page, size);
    return adminService.getAllModeratorAccountWithPaging(paging);
  }

  @GetMapping(AuthPortalEndPoint.ADMIN_GET_ACCOUNT_ALL_BY_QUERY)
  @ApiOperation(value = "Get all moderator account by account full name or email")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public PagingEntityDto getAllModeratorAccountByEmail(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam String query
  ) {
    if (page <= 1) {
      page = 0;
    } else {
      page--;
    }
    Pageable paging = PageRequest.of(page, size);
    return adminService.getAllModeratorAccountWithPagingByQuery(paging, query);
  }

  @PostMapping(AuthPortalEndPoint.MODERATOR_ADD_NEW_MODERATOR)
  @ApiOperation(value = "Add a new moderator")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void addNewModerator(@ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @RequestParam String email)
      throws ApplicationException {

    adminService.addModeratorAccount(userPrincipal.getAccountVerified(), email);
  }


}
