package com.itravel.api.portal.auth.service;

import com.itravel.api.portal.auth.payload.CreateNotificationRequestPayload;

/**
 * Define method interfaces to communicate with message queues
 */
public interface MessagingService {

  void sendNewNotificationRequestToQueue(CreateNotificationRequestPayload request);
}
