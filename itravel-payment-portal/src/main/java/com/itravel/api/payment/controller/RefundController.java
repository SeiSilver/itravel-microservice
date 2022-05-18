package com.itravel.api.payment.controller;

import com.itravel.api.payment.constraint.CommonKey;
import com.itravel.api.payment.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.payment.exception.ApplicationException;
import com.itravel.api.payment.model.dto.PagingEntityDto;
import com.itravel.api.payment.model.dto.RefundRequestDetailDto;
import com.itravel.api.payment.model.dto.RefundRequestDto;
import com.itravel.api.payment.payload.CreateRefundRequest;
import com.itravel.api.payment.service.RefundRequestService;
import com.itravel.api.payment.util.PayloadUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

@RestController
@RequestMapping(AuthPortalEndPoint.REFUND)
@RequiredArgsConstructor
@Log4j2
public class RefundController {

  private final RefundRequestService refundRequestService;
  private final PayloadUtils payloadUtils;

  @PostMapping(AuthPortalEndPoint.REFUND_CREATE)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "Create new refund request")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public RefundRequestDto create(@RequestBody CreateRefundRequest createRefundRequest) throws ApplicationException {
    payloadUtils.validateRequiredFields(createRefundRequest);
    return refundRequestService.create(createRefundRequest);
  }

  @DeleteMapping(AuthPortalEndPoint.REFUND_DELETE_BY_ID)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "delete refund request by refund request ID")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void deleteByRefundRequestId(@PathVariable Integer refundRequestId) throws ApplicationException {
    refundRequestService.deleteByRefundRequestId(refundRequestId);
  }

  @GetMapping(AuthPortalEndPoint.REFUND_GET_BY_ID)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "Get refund request by refund request ID")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public RefundRequestDetailDto getByRefundRequestId(@PathVariable Integer refundRequestId) throws ApplicationException {
    return refundRequestService.getByRefundRequestId(refundRequestId);
  }

  @GetMapping(AuthPortalEndPoint.REFUND_GET_BY_ACCOUNT_ID)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "Get all refund request by account ID")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public PagingEntityDto getAllByAccountId(
      @PathVariable Long accountId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size)
      throws ApplicationException {
    if (page <= 1) {
      page = 0;
    } else {
      page--;
    }
    Pageable paging = PageRequest.of(page, size);
    return refundRequestService.getAllByAccountId(paging, accountId);
  }

  @GetMapping(AuthPortalEndPoint.REFUND_GET_ALL)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "Get all refund request")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public PagingEntityDto getAll(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size)
      throws ApplicationException {
    if (page <= 1) {
      page = 0;
    } else {
      page--;
    }
    Pageable paging = PageRequest.of(page, size);
    return refundRequestService.getAll(paging);
  }


  @PutMapping(AuthPortalEndPoint.REFUND_ACCEPT_BY_ID)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "Accept refund request and update status to ACCEPT by refund request ID")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void verifyAcceptRefundRequestByRefundRequestId(@PathVariable Integer refundRequestId) throws ApplicationException {
    refundRequestService.verifyAcceptRefundRequestByRefundRequestId(refundRequestId);
  }

  @PutMapping(AuthPortalEndPoint.REFUND_DENY_BY_ID)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "Deny refund request and update status to DENY by refund request ID")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void verifyDenyRefundRequestByRefundRequestId(@PathVariable Integer refundRequestId,
      @RequestParam String refuseReason)
      throws ApplicationException {
    refundRequestService.verifyDenyRefundRequestByRefundRequestId(refundRequestId, refuseReason);
  }

  @GetMapping(AuthPortalEndPoint.REFUND_GET_BY_ORDER_BILL_ID)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "Get all refund request by orderBillID")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public PagingEntityDto getAllByOrderBillId(
      @RequestParam String orderBillId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size)
      throws ApplicationException {
    if (page <= 1) {
      page = 0;
    } else {
      page--;
    }
    Pageable paging = PageRequest.of(page, size);
    return refundRequestService.searchRefundByOrderBillId(paging, orderBillId);
  }

}
