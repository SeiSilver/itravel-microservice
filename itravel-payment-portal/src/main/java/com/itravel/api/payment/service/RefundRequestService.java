package com.itravel.api.payment.service;

import com.itravel.api.payment.exception.ApplicationException;
import com.itravel.api.payment.model.dto.PagingEntityDto;
import com.itravel.api.payment.model.dto.RefundRequestDetailDto;
import com.itravel.api.payment.model.dto.RefundRequestDto;
import com.itravel.api.payment.payload.CreateRefundRequest;
import org.springframework.data.domain.Pageable;

public interface RefundRequestService {

  RefundRequestDto create(CreateRefundRequest createRefundRequest) throws ApplicationException;

  void deleteByRefundRequestId(Integer refundRequestId) throws ApplicationException;

  RefundRequestDetailDto getByRefundRequestId(Integer refundRequestId) throws ApplicationException;

  PagingEntityDto getAllByAccountId(Pageable pageable, Long accountId) throws ApplicationException;

  PagingEntityDto getAll(Pageable pageable) throws ApplicationException;

  void verifyAcceptRefundRequestByRefundRequestId(Integer refundRequestId) throws ApplicationException;

  void verifyDenyRefundRequestByRefundRequestId(Integer refundRequestId, String refuseReason) throws ApplicationException;

  PagingEntityDto searchRefundByOrderBillId(Pageable pageable, String orderBillId) throws ApplicationException;
}
