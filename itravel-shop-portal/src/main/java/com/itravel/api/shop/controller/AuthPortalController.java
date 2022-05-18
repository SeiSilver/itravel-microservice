package com.itravel.api.shop.controller;

import com.itravel.api.shop.annotation.CurrentUser;
import com.itravel.api.shop.constraint.CommonKey;
import com.itravel.api.shop.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.shop.model.dto.JwtResponse;
import com.itravel.api.shop.model.entity.AccountEntity;
import com.itravel.api.shop.security.principal.AccountPrincipal;
import com.itravel.api.shop.service.AuthPortalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(AuthPortalEndPoint.AUTH)
@RequiredArgsConstructor
@Profile({"local"})
public class AuthPortalController {

  private final AuthPortalService authPortalService;

  @GetMapping(AuthPortalEndPoint.AUTH_TOKEN)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "This method is used to get the access token info. ONLY FOR TESTING")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public JwtResponse getJwtInfo(@ApiIgnore @CurrentUser AccountPrincipal userPrincipal) {
    return authPortalService.getCurrentJwt(userPrincipal.getAccessToken());
  }

  @GetMapping(AuthPortalEndPoint.AUTH_ACCOUNT)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "This method is user-info. ONLY FOR TESTING")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public AccountEntity getAccountEntity(@ApiIgnore @CurrentUser AccountPrincipal userPrincipal) {
    return authPortalService.getAccountEntityByToken(userPrincipal.getAccessToken());
  }

}
