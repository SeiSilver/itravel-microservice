package com.itravel.api.payment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

  private final MessageQueueProperties messageQueueProperties;

  @Bean
  public Queue itravelCreateOrderQueue() {
    return QueueBuilder
        .durable(messageQueueProperties.getItravelCreateOrderQueue())
        .build();
  }

  @Bean
  public Queue itravelCreateNotification() {
    return QueueBuilder
        .durable(messageQueueProperties.getItravelCreateNotification())
        .build();
  }

}
