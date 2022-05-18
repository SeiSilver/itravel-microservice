package com.itravel.api.payment.service.impl;

import com.itravel.api.payment.enums.RefundStatus;
import com.itravel.api.payment.exception.ApplicationException;
import com.itravel.api.payment.model.dto.PagingEntityDto;
import com.itravel.api.payment.model.dto.RefundRequestDetailDto;
import com.itravel.api.payment.model.dto.RefundRequestDto;
import com.itravel.api.payment.model.entity.RefundRequestEntity;
import com.itravel.api.payment.model.jpa.OrderItemRepository;
import com.itravel.api.payment.model.jpa.RefundRequestRepository;
import com.itravel.api.payment.payload.CreateNotificationRequestPayload;
import com.itravel.api.payment.payload.CreateRefundRequest;
import com.itravel.api.payment.payload.GetAccountsByIdsPayload;
import com.itravel.api.payment.service.AccountService;
import com.itravel.api.payment.service.MessagingService;
import com.itravel.api.payment.service.RefundRequestService;
import com.itravel.api.payment.util.ModelMapperUtils;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class RefundRequestServiceImpl implements RefundRequestService {

  private final RefundRequestRepository refundRequestRepository;
  private final OrderItemRepository orderItemRepository;
  private final ModelMapperUtils modelMapperUtils;
  private final AccountService accountService;
  private final MessagingService messagingService;

  @Override
  @Transactional
  public RefundRequestDto create(CreateRefundRequest createRefundRequest) throws ApplicationException {
    var orderItem = orderItemRepository.findById(createRefundRequest.getOrderItemId()).orElseThrow(
        () -> new ApplicationException(50, "OrderItem not found")
    );
    if (!Objects.equals(orderItem.getOrder().getAccountId(), createRefundRequest.getAccountId())) {
      throw new ApplicationException(53, "This account doesn't pay for this orderItem");
    }
    RefundRequestEntity request = RefundRequestEntity.builder()
        .accountId(createRefundRequest.getAccountId())
        .orderItem(orderItem)
        .requestReason(createRefundRequest.getReason())
        .reportType(createRefundRequest.getReportType())
        .status(RefundStatus.PROCESSING)
        .build();

    var tempEntity = refundRequestRepository.save(request);
    var dto = modelMapperUtils.map(tempEntity, RefundRequestDto.class);
    dto.setOrderItemCreatedAt(orderItem.getCreatedAt());
    dto.setOrderItemId(orderItem.getId());
    dto.setOrderBillId(orderItem.getOrder().getOrderBillId());
    CreateNotificationRequestPayload payload =
        CreateNotificationRequestPayload.builder()
            .receiverId(0L)
            .title("Người dùng yêu cầu hoàn tiền")
            .message("Có người dùng yêu cầu hoàn tiền các quản trị viên hãy xem xét xử lý!")
            .createdAt(ZonedDateTime.now())
            .build();
    messagingService.sendNewNotificationRequestToQueue(payload);
    return dto;
  }

  @Override
  @Transactional
  public void deleteByRefundRequestId(Integer refundRequestId) throws ApplicationException {
    var refundRequestEntity = refundRequestRepository.findById(refundRequestId).orElseThrow(
        () -> new ApplicationException(51, "Refund Request not found")
    );
    refundRequestRepository.delete(refundRequestEntity);
  }

  @Override
  public RefundRequestDetailDto getByRefundRequestId(Integer refundRequestId) throws ApplicationException {
    var tempEntity = refundRequestRepository.findById(refundRequestId).orElseThrow(
        () -> new ApplicationException(51, "Refund Request not found")
    );
    GetAccountsByIdsPayload payload = GetAccountsByIdsPayload.builder()
        .listId(List.of(tempEntity.getAccountId()))
        .build();

    var accountEntity = accountService.getAccountByAccountIds(payload).get(0);
    var dto = modelMapperUtils.map(tempEntity, RefundRequestDetailDto.class);
    dto.setOrderItemId(tempEntity.getOrderItem().getId());
    dto.setOrderItemCreatedAt(tempEntity.getOrderItem().getCreatedAt());
    dto.setShopName(tempEntity.getOrderItem().getShopName());
    dto.setUserName(accountEntity.getFullName());
    dto.setUsedStart(tempEntity.getOrderItem().getUsedStart());
    dto.setUsedEnd(tempEntity.getOrderItem().getUsedEnd());
    dto.setOrderBillId(tempEntity.getOrderItem().getOrder().getOrderBillId());

    return dto;
  }

  @Override
  public PagingEntityDto getAllByAccountId(Pageable pageable, Long accountId) throws ApplicationException {
    var pages = refundRequestRepository.findAllByAccountId(pageable, accountId);
    if (pages.isEmpty()) {
      throw new ApplicationException(99, "This account doesn't have any refund request!");
    }
    var listData = pages.getContent();
    var dto = modelMapperUtils.mapList(listData, RefundRequestDto.class);

    for (int i = 0; i < listData.size(); i++) {
      dto.get(i).setOrderItemId(listData.get(i).getOrderItem().getId());
      dto.get(i).setOrderItemCreatedAt(listData.get(i).getOrderItem().getCreatedAt());
      dto.get(i).setOrderBillId(listData.get(i).getOrderItem().getOrder().getOrderBillId());
    }

    return PagingEntityDto
        .builder()
        .page(pages.getNumber() + 1)
        .totalResults(pages.getTotalElements())
        .totalPages(pages.getTotalPages())
        .results(Collections.singletonList(dto))
        .build();
  }

  @Override
  public PagingEntityDto getAll(Pageable pageable) throws ApplicationException {
    var pages = refundRequestRepository.findAll(pageable);
    if (pages.isEmpty()) {
      throw new ApplicationException(51, "Refund Request not found");
    }
    var listData = pages.getContent();
    var dto = modelMapperUtils.mapList(listData, RefundRequestDto.class);
    for (int i = 0; i < listData.size(); i++) {
      dto.get(i).setOrderItemId(listData.get(i).getOrderItem().getId());
      dto.get(i).setOrderItemCreatedAt(listData.get(i).getOrderItem().getCreatedAt());
      dto.get(i).setOrderBillId(listData.get(i).getOrderItem().getOrder().getOrderBillId());
    }

    return PagingEntityDto
        .builder()
        .page(pages.getNumber() + 1)
        .totalResults(pages.getTotalElements())
        .totalPages(pages.getTotalPages())
        .results(Collections.singletonList(dto))
        .build();
  }

  @Override
  @Transactional
  public void verifyAcceptRefundRequestByRefundRequestId(Integer refundRequestId) throws ApplicationException {
    var tempEntity = refundRequestRepository.findById(refundRequestId).orElseThrow(
        () -> new ApplicationException(51, "Refund Request not found")
    );
    tempEntity.setStatus(RefundStatus.ACCEPT);
    refundRequestRepository.save(tempEntity);
    CreateNotificationRequestPayload payload =
        CreateNotificationRequestPayload.builder()
            .receiverId(tempEntity.getAccountId())
            .title("Phản hồi yêu cầu hoàn tiền")
            .message("Yêu cầu hoàn tiền của bạn đã được chấp nhận, chúng tôi sẽ liện hệ bạn để nhân lại tiền sớm nhất!")
            .createdAt(ZonedDateTime.now())
            .build();
    messagingService.sendNewNotificationRequestToQueue(payload);
  }

  @Override
  @Transactional
  public void verifyDenyRefundRequestByRefundRequestId(Integer refundRequestId, String refuseReason) throws ApplicationException {
    var tempEntity = refundRequestRepository.findById(refundRequestId).orElseThrow(
        () -> new ApplicationException(51, "Refund Request not found")
    );
    tempEntity.setRefuseReason(refuseReason);
    tempEntity.setStatus(RefundStatus.DENY);
    refundRequestRepository.save(tempEntity);
    CreateNotificationRequestPayload payload =
        CreateNotificationRequestPayload.builder()
            .receiverId(tempEntity.getAccountId())
            .title("Phản hồi yêu cầu hoàn tiền")
            .message("Yêu cầu hoàn tiền của bạn đã bị từ chối, bạn có xem chi tiết tại trang cá nhân, nếu thắc mắc hãy liên hệ chúng tôi!")
            .createdAt(ZonedDateTime.now())
            .build();
    messagingService.sendNewNotificationRequestToQueue(payload);
  }

  @Override
  @Transactional
  public PagingEntityDto searchRefundByOrderBillId(Pageable pageable, String orderBillId) throws ApplicationException {

    var pages = refundRequestRepository.findAllByOrderBillId(pageable, orderBillId);
    if (pages.isEmpty()) {
      throw new ApplicationException(99, "No refund request for this orderBillId!");
    }
    var listData = pages.getContent();
    var dto = modelMapperUtils.mapList(listData, RefundRequestDto.class);

    for (int i = 0; i < listData.size(); i++) {
      dto.get(i).setOrderItemId(listData.get(i).getOrderItem().getId());
      dto.get(i).setOrderItemCreatedAt(listData.get(i).getOrderItem().getCreatedAt());
      dto.get(i).setOrderBillId(listData.get(i).getOrderItem().getOrder().getOrderBillId());
    }

    return PagingEntityDto
        .builder()
        .page(pages.getNumber() + 1)
        .totalResults(pages.getTotalElements())
        .totalPages(pages.getTotalPages())
        .results(Collections.singletonList(dto))
        .build();

  }
}
