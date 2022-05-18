package com.itravel.api.portal.auth.controller;

import com.itravel.api.portal.auth.annotation.CurrentUser;
import com.itravel.api.portal.auth.constraint.CommonKey;
import com.itravel.api.portal.auth.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.portal.auth.model.dto.RoleInfo;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import com.itravel.api.portal.auth.payload.AccountUpdateRequest;
import com.itravel.api.portal.auth.security.principal.AccountPrincipal;
import com.itravel.api.portal.auth.service.AccountService;
import com.itravel.api.portal.auth.util.ModelMapperUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(AuthPortalEndPoint.ACCOUNT)
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;
  private final ModelMapperUtils modelMapperUtils;

  @GetMapping("")
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "Get current user basic info")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public AccountEntity getCurrentUserBasicInfo(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal) {
    return userPrincipal.getAccountVerified();
  }

  @PutMapping("")
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "Update current user basic info")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void updateUserBasicInfo(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @RequestBody AccountUpdateRequest accountUpdateRequest) {
    Long accountId = userPrincipal.getAccountVerified().getId();
    accountService.updateBasicInfo(accountId, accountUpdateRequest);
  }

  @GetMapping(AuthPortalEndPoint.ACCOUNT_ROLES)
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "Get all roles of current user")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public List<RoleInfo> getCurrentUserRoles(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal) {
    var accountRoles = userPrincipal.getAccountVerified().getRoles();
    return modelMapperUtils.mapList(accountRoles, RoleInfo.class);
  }

}
