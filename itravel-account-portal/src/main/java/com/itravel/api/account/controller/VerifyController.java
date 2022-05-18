package com.itravel.api.account.controller;

import com.itravel.api.account.constraint.CommonKey;
import com.itravel.api.account.controller.endpoint.AccountPortalEndPoint;
import com.itravel.api.account.exception.ApplicationException;
import com.itravel.api.account.model.dto.PagingEntityDto;
import com.itravel.api.account.model.entity.SellerInfoEntity;
import com.itravel.api.account.service.VerifySellerService;
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

@RestController
@RequestMapping(AccountPortalEndPoint.VERIFY)
@RequiredArgsConstructor
public class VerifyController {

  private final VerifySellerService verifySellerService;

  @GetMapping(AccountPortalEndPoint.VERIFY_GET_ALL_SELLER_INFO)
  @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
  @ApiOperation(value = "get all list seller info need to be verify")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public PagingEntityDto getAllSellerNotVerified(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    if (page <= 1) {
      page = 0;
    } else {
      page--;
    }
    Pageable paging = PageRequest.of(page, size);
    return verifySellerService.getAllSellerNotVerified(paging);
  }

  @GetMapping(AccountPortalEndPoint.VERIFY_GET_SELLER_INFO)
  @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
  @ApiOperation(value = "get seller info by account ID")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public SellerInfoEntity getSellerNotVerifiedByAccountId(
      @PathVariable Long accountId
  ) throws ApplicationException {
    return verifySellerService.getSellerNotVerifiedByAccountId(accountId);
  }

  @PutMapping(AccountPortalEndPoint.VERIFY_ACCEPT_SELLER_INFO)
  @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
  @ApiOperation(value = "accept seller info by account ID and add ROLE_SELLER")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void acceptSellerByAccountId(
      @PathVariable Long accountId
  ) throws ApplicationException {
    verifySellerService.acceptSellerByAccountId(accountId);
  }

  @PutMapping(AccountPortalEndPoint.VERIFY_DENY_SELLER_INFO)
  @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
  @ApiOperation(value = "deny seller info by account ID and remove ROLE_SELLER")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void denySellerByAccountId(
      @PathVariable Long accountId
  ) throws ApplicationException {
    verifySellerService.denySellerByAccountId(accountId);
  }

}
