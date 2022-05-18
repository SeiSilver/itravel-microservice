package com.itravel.api.account.service;

import com.itravel.api.account.payload.AddSellerRoleRequestPayload;
import com.itravel.api.account.payload.CreateNotificationRequestPayload;
import com.itravel.api.account.payload.UpdateRateRequest;

/**
 * Define method interfaces to communicate with message queues
 */
public interface MessagingService {

  void sendUpdateRoleRequestToQueue(AddSellerRoleRequestPayload payload);

  void sendNewNotificationRequestToQueue(CreateNotificationRequestPayload request);

  void sendUpdateServiceRate(UpdateRateRequest request);
}
