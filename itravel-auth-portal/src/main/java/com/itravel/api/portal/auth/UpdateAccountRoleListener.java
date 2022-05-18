package com.itravel.api.portal.auth;

import com.itravel.api.portal.auth.enums.RequestType;
import com.itravel.api.portal.auth.exception.ApplicationException;
import com.itravel.api.portal.auth.payload.AddSellerRoleRequestPayload;
import com.itravel.api.portal.auth.service.AccountService;
import com.itravel.api.portal.auth.util.PayloadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class UpdateAccountRoleListener {

  private final PayloadUtils payloadUtils;
  private final AccountService accountService;

  @RabbitListener(
      queues = "#{itravelUpdateStatus}",
      concurrency = "#{messageQueueProperties.listener.concurrency}",
      errorHandler = "processRequestErrorHandler",
      ackMode = "#{messageQueueProperties.listener.ackMode}"
  )
  public void process(Message message) {
    String payload = new String(message.getBody());
    AddSellerRoleRequestPayload request;
    try {
      request = payloadUtils.parseJson(payload, AddSellerRoleRequestPayload.class);
      payloadUtils.validateRequiredFields(request);

      if (request.getRequestType() == RequestType.ADD_ROLE) {
        accountService.addRole(null, request.getAccountId(), request.getRole());
      } else if (request.getRequestType() == RequestType.REMOVE_ROLE) {
        accountService.deleteRole(null, request.getAccountId(), request.getRole());
      }

    } catch (ApplicationException e) {
      log.error("error: ", e);
    }
  }

}


