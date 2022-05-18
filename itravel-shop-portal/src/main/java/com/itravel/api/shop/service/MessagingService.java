package com.itravel.api.shop.service;

import com.itravel.api.shop.payload.CreateNotificationRequestPayload;
import com.itravel.api.shop.payload.MQOrderRequest;

/**
 * Define method interfaces to communicate with message queues
 */
public interface MessagingService {

  void sendNewOrderRequestToQueue(MQOrderRequest request);

  void sendNewNotificationRequestToQueue(CreateNotificationRequestPayload request);
}
