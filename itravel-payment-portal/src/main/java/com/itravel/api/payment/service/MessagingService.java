package com.itravel.api.payment.service;

import com.itravel.api.payment.payload.CreateNotificationRequestPayload;

/**
 * Define method interfaces to communicate with message queues
 */
public interface MessagingService {

  void sendNewNotificationRequestToQueue(CreateNotificationRequestPayload request);
}
