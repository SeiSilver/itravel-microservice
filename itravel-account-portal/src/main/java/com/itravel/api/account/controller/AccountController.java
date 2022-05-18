package com.itravel.api.account.controller;

import com.itravel.api.account.annotation.CurrentUser;
import com.itravel.api.account.constraint.CommonKey;
import com.itravel.api.account.controller.endpoint.AccountPortalEndPoint;
import com.itravel.api.account.exception.MissingRequireFieldException;
import com.itravel.api.account.exception.S3UploadErrorException;
import com.itravel.api.account.exception.SellerInfoExistedException;
import com.itravel.api.account.exception.SellerInfoNotFoundException;
import com.itravel.api.account.model.dto.NotificationInfo;
import com.itravel.api.account.model.dto.SellerInfoResponse;
import com.itravel.api.account.payload.SellerInfoRequest;
import com.itravel.api.account.security.principal.AccountPrincipal;
import com.itravel.api.account.service.NotificationService;
import com.itravel.api.account.service.SellerInfoService;
import com.itravel.api.account.util.PayloadUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(AccountPortalEndPoint.ACCOUNT)
@RequiredArgsConstructor
public class AccountController {

  private final NotificationService notificationService;
  private final SellerInfoService sellerInfoService;
  private final PayloadUtils payloadUtils;

  @GetMapping(AccountPortalEndPoint.ACCOUNT_SELLER_INFO)
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "Get all seller info of current user")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public SellerInfoResponse getCurrentUserSellerInfo(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal)
      throws SellerInfoNotFoundException {
    var accountId = userPrincipal.getAccountVerified().getId();
    return sellerInfoService.getSellerInfoByAccountId(accountId);
  }

  @PostMapping(AccountPortalEndPoint.ACCOUNT_SELLER_INFO)
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "Register seller info in current user")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public SellerInfoResponse createCurrentUserSellerInfo(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @RequestBody SellerInfoRequest sellerInfoRequest
  ) throws MissingRequireFieldException, SellerInfoExistedException, S3UploadErrorException {
    payloadUtils.validateRequiredFields(sellerInfoRequest);
    return sellerInfoService.create(userPrincipal.getAccountVerified(), sellerInfoRequest);
  }

  @PutMapping(AccountPortalEndPoint.ACCOUNT_SELLER_INFO)
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "Update seller info in current user")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void updateCurrentUserSellerInfo(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @RequestBody SellerInfoRequest sellerInfoRequest
  ) throws MissingRequireFieldException, SellerInfoNotFoundException, S3UploadErrorException {
    payloadUtils.validateRequiredFields(sellerInfoRequest);
    sellerInfoService.update(userPrincipal.getAccountVerified(), sellerInfoRequest);
  }

  @GetMapping(AccountPortalEndPoint.ACCOUNT_NOTIFICATION)
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "get account notification")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public List<NotificationInfo> getNotificationByAccountId(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal
  ) {
    return notificationService.getListNotificationByAccountId(userPrincipal.getAccountVerified());
  }

}