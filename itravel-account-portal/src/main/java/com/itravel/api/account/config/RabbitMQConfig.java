package com.itravel.api.account.config;

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
  public Queue itravelCreateNotification() {
    return QueueBuilder
        .durable(messageQueueProperties.getItravelCreateNotification())
        .build();
  }

  @Bean
  public Queue itravelUpdateStatus() {
    return QueueBuilder
        .durable(messageQueueProperties.getItravelUpdateStatus())
        .build();
  }

  @Bean
  public Queue itravelCreateUpdateRateQueue() {
    return QueueBuilder
        .durable(messageQueueProperties.getItravelUpdateRateQueue())
        .build();
  }
}
