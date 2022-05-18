package com.itravel.api.account.service.impl;

import com.itravel.api.account.constraint.ErrorCode;
import com.itravel.api.account.constraint.ErrorMessage;
import com.itravel.api.account.enums.RequestType;
import com.itravel.api.account.enums.RoleType;
import com.itravel.api.account.exception.ApplicationException;
import com.itravel.api.account.model.dto.PagingEntityDto;
import com.itravel.api.account.model.entity.SellerInfoEntity;
import com.itravel.api.account.model.jpa.SellerInfoRepository;
import com.itravel.api.account.payload.AddSellerRoleRequestPayload;
import com.itravel.api.account.payload.CreateNotificationRequestPayload;
import com.itravel.api.account.service.MessagingService;
import com.itravel.api.account.service.VerifySellerService;
import java.time.ZonedDateTime;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class VerifySellerServiceImpl implements VerifySellerService {

  private final SellerInfoRepository sellerInfoRepository;
  private final MessagingService messagingService;

  @Override
  public PagingEntityDto getAllSellerNotVerified(Pageable pageable) {
    Page<SellerInfoEntity> pages = sellerInfoRepository.findAllByIsVerifiedIsNullOrIsVerifiedFalse(pageable);
    var content = pages.getContent();
    return PagingEntityDto
        .builder()
        .page(pages.getNumber() + 1)
        .totalResults(pages.getTotalElements())
        .totalPages(pages.getTotalPages())
        .results(Collections.singletonList(content))
        .build();
  }

  @Override
  public SellerInfoEntity getSellerNotVerifiedByAccountId(Long accountId) throws ApplicationException {
    return sellerInfoRepository.findByAccountId(accountId).orElseThrow(
        () -> new ApplicationException(ErrorCode.SELLER_INFO_NOT_FOUND, ErrorMessage.SELLER_INFO_NOT_FOUND)
    );
  }

  @Override
  @Transactional
  public void acceptSellerByAccountId(Long accountId) throws ApplicationException {
    var sellerInfoEntity = getSellerNotVerifiedByAccountId(accountId);
    sellerInfoEntity.setIsVerified(true);
    sellerInfoRepository.save(sellerInfoEntity);

    // update seller role
    AddSellerRoleRequestPayload addSellerRoleRequestPayload = AddSellerRoleRequestPayload.builder()
        .accountId(accountId)
        .requestType(RequestType.ADD_ROLE)
        .role(RoleType.ROLE_SELLER)
        .build();
    messagingService.sendUpdateRoleRequestToQueue(addSellerRoleRequestPayload);
    CreateNotificationRequestPayload payload =
        CreateNotificationRequestPayload.builder()
            .receiverId(accountId)
            .title("Phản hồi yêu cầu đăng ký bán hàng")
            .message("Yêu cầu đăng kí làm người bán hàng của bạn đã được xác minh!")
            .createdAt(ZonedDateTime.now())
            .build();
    messagingService.sendNewNotificationRequestToQueue(payload);
  }

  @Override
  @Transactional
  public void denySellerByAccountId(Long accountId) throws ApplicationException {
    var sellerInfoEntity = getSellerNotVerifiedByAccountId(accountId);
    sellerInfoEntity.setIsVerified(false);
    sellerInfoRepository.save(sellerInfoEntity);

    AddSellerRoleRequestPayload addSellerRoleRequestPayload = AddSellerRoleRequestPayload.builder()
        .accountId(sellerInfoEntity.getId())
        .requestType(RequestType.REMOVE_ROLE)
        .role(RoleType.ROLE_SELLER)
        .build();
    messagingService.sendUpdateRoleRequestToQueue(addSellerRoleRequestPayload);
    CreateNotificationRequestPayload payload =
        CreateNotificationRequestPayload.builder()
            .receiverId(accountId)
            .title("Phản hồi yêu cầu đăng ký bán hàng")
            .message("Yêu cầu đăng kí làm người bán hàng của bạn đã bị từ chối, vui lòng kiểm tra thông tin của bạn!")
            .createdAt(ZonedDateTime.now())
            .build();
    messagingService.sendNewNotificationRequestToQueue(payload);
  }

}
