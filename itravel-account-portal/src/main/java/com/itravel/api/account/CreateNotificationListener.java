package com.itravel.api.account;

import com.itravel.api.account.exception.InvalidJsonException;
import com.itravel.api.account.exception.MissingRequireFieldException;
import com.itravel.api.account.payload.CreateNotificationRequestPayload;
import com.itravel.api.account.service.NotificationService;
import com.itravel.api.account.util.PayloadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class CreateNotificationListener {

  private final NotificationService notificationService;
  private final PayloadUtils payloadUtils;

  @RabbitListener(
      queues = "#{itravelCreateNotification}",
      concurrency = "#{messageQueueProperties.listener.concurrency}",
      errorHandler = "processRequestErrorHandler",
      ackMode = "#{messageQueueProperties.listener.ackMode}"
  )
  public void process(Message message) {
    String payload = new String(message.getBody());
    CreateNotificationRequestPayload request;
    try {
      request = payloadUtils.parseJson(payload, CreateNotificationRequestPayload.class);
      payloadUtils.validateRequiredFields(request);
      notificationService.create(request);
    } catch (MissingRequireFieldException | InvalidJsonException e) {
      log.error("error: ", e);
    }
  }

}


