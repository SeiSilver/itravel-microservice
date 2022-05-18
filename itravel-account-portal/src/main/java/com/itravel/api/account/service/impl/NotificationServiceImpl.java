package com.itravel.api.account.service.impl;

import com.itravel.api.account.enums.RoleType;
import com.itravel.api.account.model.dto.NotificationInfo;
import com.itravel.api.account.model.entity.NotificationEntity;
import com.itravel.api.account.model.entitydto.AccountEntity;
import com.itravel.api.account.model.entitydto.RoleEntity;
import com.itravel.api.account.model.jpa.NotificationRepository;
import com.itravel.api.account.payload.CreateNotificationRequestPayload;
import com.itravel.api.account.service.NotificationService;
import com.itravel.api.account.util.ModelMapperUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final ModelMapperUtils modelMapperUtils;

  @Override
  public List<NotificationInfo> getListNotificationByAccountId(AccountEntity accountVerified) {

    List<RoleType> roles = accountVerified.getRoles().stream().map(RoleEntity::getRoleName).collect(Collectors.toList());

    if (roles.contains(RoleType.ROLE_MODERATOR) || roles.contains(RoleType.ROLE_ADMIN)) {
      return modelMapperUtils.mapList(notificationRepository.getAllByReceiverIdForModAndAdmin(accountVerified.getId()), NotificationInfo.class);
    }
    return modelMapperUtils.mapList(notificationRepository.getAllByReceiverIdOrderByCreatedAtDesc(accountVerified.getId()), NotificationInfo.class);
  }

  @Override
  @Transactional
  public NotificationEntity create(CreateNotificationRequestPayload request) {
    NotificationEntity notificationEntity = NotificationEntity.builder()
        .receiverId(request.getReceiverId())
        .title(request.getTitle())
        .message(request.getMessage())
        .hasSeen(false)
        .createdAt(request.getCreatedAt())
        .build();
    return notificationRepository.save(notificationEntity);
  }

}
