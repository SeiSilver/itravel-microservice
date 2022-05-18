package com.itravel.api.portal.auth.controller;

import com.itravel.api.portal.auth.annotation.CurrentUser;
import com.itravel.api.portal.auth.constraint.CommonKey;
import com.itravel.api.portal.auth.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.portal.auth.exception.ApplicationException;
import com.itravel.api.portal.auth.model.dto.PagingEntityDto;
import com.itravel.api.portal.auth.security.principal.AccountPrincipal;
import com.itravel.api.portal.auth.service.ModeratorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(AuthPortalEndPoint.MODERATOR)
@RequiredArgsConstructor
@PreAuthorize("hasRole('MODERATOR')")
public class ModeratorController {

  private final ModeratorService moderatorService;

  @PutMapping(AuthPortalEndPoint.MODERATOR_LOCK_ACCOUNT_BY_ACCOUNT_ID)
  @ApiOperation(value = "Lock user, seller account by Id")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void lockAccount(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @PathVariable Long accountId) throws ApplicationException {
    moderatorService.lockAccount(userPrincipal.getAccountVerified(), accountId);
  }

  @PutMapping(AuthPortalEndPoint.MODERATOR_UNLOCK_ACCOUNT_BY_ACCOUNT_ID)
  @ApiOperation(value = "Unlock user, seller account by Id")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void unlockAccount(@ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @PathVariable Long accountId) throws ApplicationException {
    moderatorService.unlockAccount(userPrincipal.getAccountVerified(), accountId);
  }

  @GetMapping(AuthPortalEndPoint.MODERATOR_GET_ACCOUNT_ALL)
  @ApiOperation(value = "Get all user, seller account")
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
    return moderatorService.getAllUserSellerAccountWithPaging(paging);
  }

  @GetMapping(AuthPortalEndPoint.MODERATOR_GET_ACCOUNT_ALL_BY_QUERY)
  @ApiOperation(value = "Get all user, seller account by account full name or email")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public PagingEntityDto getAllAccountByQuery(
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
    return moderatorService.getAllUserSellerAccountWithPagingByQuery(paging, query);
  }

}
