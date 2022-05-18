package com.itravel.api.payment;

import com.itravel.api.payment.controller.CreateOrderController;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderListener {

  private final CreateOrderController createOrderController;

  @RabbitListener(
      queues = "#{itravelCreateOrderQueue}",
      concurrency = "#{messageQueueProperties.listener.concurrency}",
      errorHandler = "processRequestErrorHandler",
      ackMode = "#{messageQueueProperties.listener.ackMode}"
  )
  public void process(Message message) {
    createOrderController.process(new String(message.getBody()));
  }

}


