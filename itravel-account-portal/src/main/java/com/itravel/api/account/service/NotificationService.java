package com.itravel.api.account.service;

import com.itravel.api.account.model.dto.NotificationInfo;
import com.itravel.api.account.model.entity.NotificationEntity;
import com.itravel.api.account.model.entitydto.AccountEntity;
import com.itravel.api.account.payload.CreateNotificationRequestPayload;
import java.util.List;

public interface NotificationService {

  List<NotificationInfo> getListNotificationByAccountId(AccountEntity accountVerified);

  NotificationEntity create(CreateNotificationRequestPayload request);

}
