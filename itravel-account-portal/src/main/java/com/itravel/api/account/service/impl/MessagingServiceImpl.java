package com.itravel.api.account.service.impl;

import com.itravel.api.account.config.MessageQueueProperties;
import com.itravel.api.account.payload.AddSellerRoleRequestPayload;
import com.itravel.api.account.payload.CreateNotificationRequestPayload;
import com.itravel.api.account.payload.UpdateRateRequest;
import com.itravel.api.account.service.MessagingService;
import com.itravel.api.account.util.PayloadUtils;
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
  public void sendUpdateRoleRequestToQueue(AddSellerRoleRequestPayload request) {
    String payload = payloadUtils.parseObjectToString(request);
    log.info("Send create order request with payload: {}", payload);
    rabbitTemplate.convertAndSend(messagingProperties.getItravelUpdateStatus(), payload);
    log.info("Send create order request success");
  }

  @Override
  public void sendNewNotificationRequestToQueue(CreateNotificationRequestPayload request) {
    String payload = payloadUtils.parseObjectToString(request);
    log.info("Send create notification request with payload: {}", payload);
    rabbitTemplate.convertAndSend(messagingProperties.getItravelCreateNotification(), payload);
    log.info("Send create notification request success");
  }

  @Override
  public void sendUpdateServiceRate(UpdateRateRequest request) {
    String payload = payloadUtils.parseObjectToString(request);
    log.info("Send update service rate request with payload: {}", payload);
    rabbitTemplate.convertAndSend(messagingProperties.getItravelUpdateRateQueue(), payload);
    log.info("Send update service rate request success");
  }

}
