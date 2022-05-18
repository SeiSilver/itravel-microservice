package com.itravel.api.portal.auth.service.impl;

import com.itravel.api.portal.auth.config.MessageQueueProperties;
import com.itravel.api.portal.auth.payload.CreateNotificationRequestPayload;
import com.itravel.api.portal.auth.service.MessagingService;
import com.itravel.api.portal.auth.util.PayloadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {

  private final RabbitTemplate rabbitTemplate;
  private final MessageQueueProperties messagingProperties;
  private final PayloadUtils payloadUtils;

  @Override
  public void sendNewNotificationRequestToQueue(CreateNotificationRequestPayload request) {
    String payload = payloadUtils.parseObjectToString(request);
    log.info("Send create notification request with payload: {}", payload);
    rabbitTemplate.convertAndSend(messagingProperties.getItravelCreateNotification(), payload);
    log.info("Send create notification request success");
  }

}
